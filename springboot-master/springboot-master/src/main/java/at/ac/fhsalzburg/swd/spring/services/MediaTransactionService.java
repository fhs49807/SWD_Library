package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
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

		// Fetch user, media, and edition
		User user = userService.getByUsername(username);
		Media media = mediaService.findById(mediaId);
		Collection<Edition> availableEditions = editionService.findByMediaAndAvailable(media);
		Edition selectedEdition = editionService.findFirstAvailableEdition(availableEditions);

		// Calculate return dates 
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

	    // 1. Setze das Rückgabedatum
	    transaction.setReturnDate(new Date());

	    // 2. Berechne das Strafgeld, wenn die Rückgabe verspätet ist
	    double penalty = calculatePenalty(transaction);
	    User user = transaction.getUser();
	    if (user.getCredit() < penalty) {
	        throw new IllegalStateException("User does not have enough credit to pay the penalty");
	    }

	    // 3. Deduktion der Kosten
	    user.setCredit(user.getCredit() - (long) penalty);
	    invoiceService.deductAmount(user, transaction); // Update invoice and credit deduction

	    // 4. Setze den Status der Transaktion auf "COMPLETED"
	    transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

	    // 5. Stelle sicher, dass die Edition wieder verfügbar ist
	    Edition edition = transaction.getEdition();
	    edition.setAvailable(true); // Die Edition wird wieder verfügbar

	    // 6. Keine Notwendigkeit für `editionRepository.save(edition)` oder `mediaTransactionRepository.save(transaction)`
	    // Das ORM kümmert sich um die Persistenz, weil die Entitäten bereits im Kontext des EntityManagers sind.
	}

	private double calculatePenalty(MediaTransaction transaction) {
		if (transaction.getReturnDate() != null
				&& transaction.getReturnDate().after(transaction.getLast_possible_return_date())) {
			long diffInMillis = transaction.getReturnDate().getTime()
					- transaction.getLast_possible_return_date().getTime();
			long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
			return diffInDays > 0 ? diffInDays * 1.0 : 0.0; // €1 per overdue day
		}
		return 0.0;
	}
}
