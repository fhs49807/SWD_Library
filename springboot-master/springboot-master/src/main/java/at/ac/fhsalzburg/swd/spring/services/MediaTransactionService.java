package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaTransactionService implements MediaTransactionServiceInterface {

    @Autowired
    private MediaTransactionRepository mediaTransactionRepository;

    @Autowired
    private EditionRepository editionRepository;

    @Autowired
    private InvoiceServiceInterface invoiceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private MediaRepository mediaRepository;

    // creates loan record for a customer
    // marks each loaned item as unavailable
    // saves transaction to repository
    @Override
    public Collection<MediaTransaction> createLoanRecord(User user, Date dueDate, Collection<Edition> editions) {
        Collection<MediaTransaction> transactions = editions.stream().map(edition -> {
            MediaTransaction loan = new MediaTransaction(new Date(), dueDate, Collections.emptyList(), edition, user);
            edition.setAvailable(false); // Mark the edition as unavailable
            editionRepository.save(edition); // Persist availability change
            return mediaTransactionRepository.save(loan);
        }).collect(Collectors.toList());
        return transactions;
    }

    // finds all loan transactions for specific customer
    @Override
    public Collection<MediaTransaction> findLoansByUser(User user) {
        return mediaTransactionRepository.findByUser(user);
    }

    // get all loans currently entered in the system
    @Override
    public Collection<MediaTransaction> getAllLoans() {
        return (Collection<MediaTransaction>) mediaTransactionRepository.findAll();
    }

    @Override
    public MediaTransaction loanMedia(String username, Long mediaId, Date dueDate) {
        // Validate dueDate
        LocalDate todayDate = LocalDate.now();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = todayDate.plusDays(1);

        // Validierung des dueDate (muss mindestens heute sein)
        if (dueLocalDate.isBefore(todayDate)) {
            throw new IllegalArgumentException("Date cannot be in the past.");
        }

        // Find user
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Fetch media and available editions
        Media media = mediaRepository.findById(mediaId)
            .orElseThrow(() -> new IllegalArgumentException("Media not found."));
        List<Edition> availableEditions = editionRepository.findByMediaAndAvailable(media);

        if (availableEditions.isEmpty()) {
            throw new IllegalArgumentException("No available editions for the selected media!");
        }

        // Select the first available edition
        Edition selectedEdition = availableEditions.get(0);

        // Validate loan status
        boolean isAlreadyLoaned = mediaTransactionRepository.existsByUserAndEditionAndStatus(user, selectedEdition,
            MediaTransaction.TransactionStatus.ACTIVE);
        if (isAlreadyLoaned) {
            throw new IllegalStateException("User already has this edition on loan.");
        }

        // Proceed with loaning
        Date transactionDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transactionDate);
        calendar.add(Calendar.DAY_OF_YEAR, 14); // 14-day loan period
        Date expectedReturnDate = calendar.getTime();

        // Create and save the transaction
        MediaTransaction transaction = new MediaTransaction(transactionDate, dueDate, selectedEdition, user);
        transaction.setExpectedReturnDate(expectedReturnDate);
        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        mediaTransactionRepository.save(transaction);

        // Update edition availability
        selectedEdition.setAvailable(false);
        selectedEdition.setDueDate(expectedReturnDate);
        editionRepository.save(selectedEdition);

        return transaction;
    }

    //ruft die transaktion auf, aktualisiert den status & berechnet gebühren -> InvoiceService calculatePenalty()
    @Override
    public void returnMedia(Long transactionId) {
        // sucht die transaktion anhand der id
        MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("transaction not found"));

        // setzt das rückgabedatum auf das aktuelle datum
        transaction.setReturnDate(new Date());
        // ändert den status der transaktion auf "completed"
        transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

        // holt die edition, die mit der transaktion verknüpft ist
        Edition edition = transaction.getEdition();
        if (edition != null) {
            edition.setAvailable(true); // markiert die edition als verfügbar
            edition.setDueDate(null);   // setzt das rückgabedatum der edition zurück
            editionRepository.save(
                edition); // speichert die aktualisierte edition in der datenbank -> MediaTransactionRepo findByID()
        }

        // berechnet die mahngebühren für die transaktion
        double penaltyAmount = invoiceService.calculatePenalty(transaction);

        User user =
            transaction.getUser();  // holt den benutzer, der die transaktion durchgeführt hat zur guthaben
        // aktualisierung -> user.setCredit(user.getCredit() - (long) penaltyAmount)
        if (user.getCredit() < penaltyAmount) {
            // prüft, ob der benutzer genug guthaben hat, um die mahngebühren zu zahlen
            throw new IllegalStateException("nicht genügend guthaben auf dem konto, um die mahngebühren zu bezahlen.");
        }

        user.setCredit(user.getCredit() -
                       (long) penaltyAmount); // wenn rückgabe gebührenpflichtig, prüfung ob genug guthaben & zieht
        // die mahngebühr vom guthaben des benutzers ab -> userService.updateUser(user);
        userService.updateUser(user); // aktualisiert die benutzerdaten in der datenbank -> TemplateController postmedia

        mediaTransactionRepository.save(
            transaction); // aktualisiert die transaktion nach der rückgabe ->  User user = transaction.getUser()

        // erstellt und speichert eine rechnung für den benutzer
        invoiceService.deductAmount(user, transaction);
    }


}
