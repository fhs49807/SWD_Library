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

	// ruft die transaktion auf, aktualisiert den status & berechnet gebühren ->
	// InvoiceService calculatePenalty()
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
			edition.setDueDate(null); // setzt das rückgabedatum der edition zurück
			editionRepository.save(edition); // speichert die aktualisierte edition in der datenbank ->
												// MediaTransactionRepo findByID()
		}

		// berechnet die mahngebühren für die transaktion
		double penaltyAmount = invoiceService.calculatePenalty(transaction);

		User user = transaction.getUser(); // holt den benutzer, der die transaktion durchgeführt hat zur guthaben
		// aktualisierung -> user.setCredit(user.getCredit() - (long) penaltyAmount)
		if (user.getCredit() < penaltyAmount) {
			// prüft, ob der benutzer genug guthaben hat, um die mahngebühren zu zahlen
			throw new IllegalStateException("nicht genügend guthaben auf dem konto, um die mahngebühren zu bezahlen.");
		}

		user.setCredit(user.getCredit() - (long) penaltyAmount); // wenn rückgabe gebührenpflichtig, prüfung ob genug
																	// guthaben & zieht
		// die mahngebühr vom guthaben des benutzers ab -> userService.updateUser(user);
		userService.updateUser(user); // aktualisiert die benutzerdaten in der datenbank -> TemplateController
										// postmedia

		mediaTransactionRepository.save(transaction); // aktualisiert die transaktion nach der rückgabe -> User user =
														// transaction.getUser()

		// erstellt und speichert eine rechnung für den benutzer
		invoiceService.deductAmount(user, transaction);
	}

}
