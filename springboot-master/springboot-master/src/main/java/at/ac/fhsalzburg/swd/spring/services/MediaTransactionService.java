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
	
	@Override
    public MediaTransaction findById(Long transactionId) {
        return mediaTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

	// get all loans currently entered in the system
	@Override
	public Collection<MediaTransaction> getAllLoans() {
		return (Collection<MediaTransaction>) mediaTransactionRepository.findAll();
	}

	@Override
	public MediaTransaction loanMedia(String username, Long mediaId, Date end_date) {
		// Validate end_date
		LocalDate todayDate = LocalDate.now();
		LocalDate endDateLocal = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (endDateLocal.isBefore(todayDate)) {
			throw new IllegalArgumentException("End date cannot be in the past.");
		}

		// Find user by username
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new IllegalArgumentException("User not found.");
		}

		// get media by id and first available edition
		Media media = mediaRepository.findById(mediaId)
				.orElseThrow(() -> new IllegalArgumentException("Media not found."));
		Edition selectedEdition = editionRepository.findByMediaAndAvailable(media).stream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No available editions."));

		// Calculate last_possible_return_date based on customerType
		int maxLoanDays = user.getCustomerType() == User.CustomerType.STUDENT ? 42 : 28; // if student --> 42 days else
																							// 28 days loan period
		LocalDate lastPossibleReturnDateLocal = todayDate.plusDays(maxLoanDays);
		Date lastPossibleReturnDate = Date
				.from(lastPossibleReturnDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Create the transaction with calculatged dates
		MediaTransaction transaction = new MediaTransaction(new Date(), // current date (transaction date)
				lastPossibleReturnDate, // calculated accrding to customerType
				selectedEdition, // selected edition for transaction
				user// user performing transaction
		);
		transaction.setEnd_date(Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));// user
																											// selected
		transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);// set transaction status

		// save transaction to DB
		mediaTransactionRepository.save(transaction);

		// Update edition availability and dueDate
		selectedEdition.setAvailable(false);// mark as unavailable
		selectedEdition.setDueDate(transaction.getEnd_date());// user selected
		editionRepository.save(selectedEdition);

		return transaction;
	}

	// Methode zur Rückgabe eines Mediums
    @Override
    public void returnMedia(Long transactionId) {
        // 1. Hole die Transaktion basierend auf der ID
        MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        // 2. Setze das Rückgabedatum auf das aktuelle Datum
        transaction.setReturnDate(new Date());

        // 3. Berechne die Strafe, falls vorhanden
        double penalty = calculatePenalty(transaction);

        // 4. Berechne die Gebühren und aktualisiere das Guthaben des Benutzers
        User user = transaction.getUser();
        if (user.getCredit() < penalty) {
            throw new IllegalStateException("User does not have enough credit to pay the penalty");
        }
        user.setCredit(user.getCredit() - (long) penalty);
        invoiceService.deductAmount(user, transaction);

        // 5. Setze den Transaktionsstatus auf 'COMPLETED'
        transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

        // 6. Update der Transaktion in der Datenbank
        mediaTransactionRepository.save(transaction);

        // 7. Update der Edition, die jetzt wieder verfügbar ist
        Edition edition = transaction.getEdition();
        edition.setAvailable(true);
        edition.setDueDate(null); // Setze das Fälligkeitsdatum auf null
        // Hier wird davon ausgegangen, dass du die Edition ebenfalls aktualisieren möchtest
    }

    // Berechnet die Strafe für verspätete Rückgaben
    private double calculatePenalty(MediaTransaction transaction) {
        // Wenn das Rückgabedatum nach dem Fälligkeitsdatum liegt, berechne die Strafe
        if (transaction.getReturnDate() != null && transaction.getReturnDate().after(transaction.getLast_possible_return_date())) {
            long diffInMillies = transaction.getReturnDate().getTime() - transaction.getLast_possible_return_date().getTime();
            long diffInDays = diffInMillies / (1000 * 60 * 60 * 24); // Differenz in Tagen
            return diffInDays * 1.0; // Angenommene Gebühr von 1 Euro pro verspätetem Tag
        }
        return 0; // Keine Strafe, wenn die Rückgabe pünktlich ist
    }

}
