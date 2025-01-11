package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MediaTransactionService implements MediaTransactionServiceInterface {

	private final MediaTransactionRepository mediaTransactionRepository;
	private final MediaServiceInterface mediaService;
	private final EditionServiceInterface editionService;
	private final InvoiceServiceInterface invoiceService;
	private final UserServiceInterface userService;

	public MediaTransactionService(MediaTransactionRepository mediaTransactionRepository,
		MediaServiceInterface mediaService, EditionServiceInterface editionService,
		InvoiceServiceInterface invoiceService, UserServiceInterface userService) {
		this.mediaTransactionRepository = mediaTransactionRepository;
		this.mediaService = mediaService;
		this.editionService = editionService;
		this.invoiceService = invoiceService;
		this.userService = userService;
	}

	@Value("${penalty.per.day:1.0}") // Standardwert von 1.0 falls nicht gesetzt
	private double penaltyPerDay;

	@Override
	public Collection<MediaTransaction> findLoansByUser(User user) {
		return mediaTransactionRepository.findByUser(user);
	}

	@Override
	public MediaTransaction findById(Long transactionId) {
		return mediaTransactionRepository.findById(transactionId)
			.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
	}

	@Override
	public Collection<MediaTransaction> getAllLoans() {
		return StreamSupport.stream(mediaTransactionRepository.findAll().spliterator(), false)
			.collect(Collectors.toList());
	}

	@Override
	public MediaTransaction loanMedia(String username, Long mediaId, Date endDate) {
		// Validate end date
		LocalDate todayDate = LocalDate.now();
		LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (endDateLocal.isBefore(todayDate)) {
			throw new IllegalArgumentException("End date cannot be in the past.");
		}

		// Fetch media and edition
		Media media = mediaService.findById(mediaId);
		Collection<Edition> availableEditions = editionService.findByMediaAndAvailable(media);
		Edition selectedEdition = editionService.findFirstAvailableEdition(availableEditions);

		if (selectedEdition == null) {
			// no more available editions left
			throw new NoSuchElementException("No more editions available for loan");
		}

		// Calculate return dates
		User user = userService.getByUsername(username);
		int maxLoanDays = user.getCustomerType() == User.CustomerType.STUDENT ? 42 : 28;
		LocalDate lastPossibleReturnDateLocal = todayDate.plusDays(maxLoanDays);
		Date lastPossibleReturnDate = Date
			.from(lastPossibleReturnDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

		// Create and save transaction
		MediaTransaction transaction = new MediaTransaction(new Date(), lastPossibleReturnDate, selectedEdition, user);
		transaction.setEnd_date(Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);
		mediaTransactionRepository.save(transaction);

		// Update edition
		editionService.markEditionAsUnavailable(selectedEdition, transaction.getEnd_date());

		return transaction;
	}

	@Override
	public void returnMedia(Long transactionId) {
		MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
			.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		// Setze das Rückgabedatum
		transaction.setReturnDate(new Date());

		// Berechne die Strafe (falls verspätet)
		double penalty = calculatePenalty(transaction);

		// Hole den Benutzer
		User user = transaction.getUser();

		// Überprüfe, ob der Benutzer genügend Kredit hat
		if (user.getCredit() < penalty) {
			throw new IllegalStateException("User does not have enough credit to pay the penalty");
		}

		// Ziehe das Strafgeld vom Benutzerkonto ab
		user.setCredit(user.getCredit() - (long) penalty);

		// Erstelle die Rechnung
		invoiceService.deductAmount(user, transaction);

		// Setze den Status der Transaktion auf "COMPLETED"
		transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

		// Mache die Edition wieder verfügbar
		Edition edition = transaction.getEdition();
		edition.setAvailable(true); // Die Edition wird wieder verfügbar

		// Das ORM kümmert sich um das Speichern der Änderungen
	}

	private double calculatePenalty(MediaTransaction transaction) {
		// Wenn das Rückgabedatum nach dem fälligen Rückgabedatum liegt, berechne die Strafe
		if (transaction.getReturnDate() != null &&
		    transaction.getReturnDate().after(transaction.getLast_possible_return_date())) {
			long diffInMillis =
				transaction.getReturnDate().getTime() - transaction.getLast_possible_return_date().getTime();
			long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
			return diffInDays > 0 ? diffInDays * penaltyPerDay : 0.0; // Dynamische Gebühr pro Tag
		}
		return 0.0;
	}
}
