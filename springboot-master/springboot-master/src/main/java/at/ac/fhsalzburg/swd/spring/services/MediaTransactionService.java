package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.enums.CustomerType;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.temporal.ChronoUnit.DAYS;

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
		return mediaTransactionRepository.findByUserAndStatus(user, TransactionStatus.LOANED);
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
	@Transactional
	public MediaTransaction loanMedia(String username, Long mediaId, LocalDate endDate) {
		// Check if a transaction is active at the start of the method
		System.out.println("Transaction open at start of loanMedia?: "
		                   + TransactionSynchronizationManager.isActualTransactionActive());

		User user = userService.getByUsername(username);

		// Validate end date
		LocalDate todayDate = LocalDate.now();
		if (endDate.isBefore(todayDate)) {
			throw new IllegalArgumentException("End date cannot be in the past.");
		}

		// Fetch media and edition
		Media media = mediaService.findById(mediaId);

		// Check if editions are available for reservation
		List<Edition> availableEditions = editionService.findAvailableEditions(media, LocalDate.now(), endDate);

		if (availableEditions.isEmpty()) {
			throw new NoSuchElementException(String.format(
				"No available editions for media: %s (%s). Please select another time period. The first possible"
				+ " start date would be on %s",
				media.getName(), media.getMediaType().getType(),
				findFirstAvailableDateForMedia(media, LocalDate.now(), endDate)));
		}
		Edition edition = availableEditions.get(0);

		// Calculate return dates
		int maxLoanDays = user.getCustomerType() == CustomerType.STUDENT ? 42 : 28;
		LocalDate lastPossibleReturnDate = todayDate.plusDays(maxLoanDays);

		// Create and save transaction
		MediaTransaction transaction = new MediaTransaction(LocalDate.now(), endDate, lastPossibleReturnDate, edition,
			user, null, null, TransactionStatus.LOANED);
		mediaTransactionRepository.save(transaction);

		return transaction;
	}

	@Override
	public void returnMedia(Long transactionId) {
		MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
			.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		// Setze das Rückgabedatum
		transaction.setReturnDate(LocalDate.now());

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
		transaction.setStatus(TransactionStatus.COMPLETED);

		// Das ORM kümmert sich um das Speichern der Änderungen
	}

	@Override
	public MediaTransaction getLatestReservation(Long mediaId, String username) {
		return mediaTransactionRepository.findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(mediaId, username)
			.orElseThrow(() -> new IllegalArgumentException("No reservation found for the given user and media."));
	}

	@Override
	@Transactional
	public MediaTransaction reserveMediaForCustomer(String userName, Long mediaId, LocalDate reserveStartDate,
		LocalDate reserveEndDate) throws IllegalStateException, NoSuchElementException {
		System.out.println("Transaction open at start of reserveMediaForCustomer?: "
		                   + TransactionSynchronizationManager.isActualTransactionActive());

		User user = userService.getByUsername(userName);
		Media media = mediaService.findById(mediaId);

		// Check if editions are available for reservation
		List<Edition> availableEditions = editionService.findAvailableEditions(media, reserveStartDate,
			reserveEndDate);
		if (availableEditions.isEmpty()) {
			throw new NoSuchElementException(String.format(
				"No available editions for media: %s (%s). Please select another time period. The first possible"
				+ " start date would be on %s",
				media.getName(), media.getMediaType().getType(),
				findFirstAvailableDateForMedia(media, reserveStartDate, reserveEndDate)));
		}

		// Reserve the first available edition
		int maxLoanDays = user.getCustomerType() == CustomerType.STUDENT ? 42 : 28;
		LocalDate lastPossibleReturnDate = reserveStartDate.plusDays(maxLoanDays);

		MediaTransaction reservation = new MediaTransaction(null, null, lastPossibleReturnDate,
			availableEditions.get(0), user, reserveStartDate, reserveEndDate, TransactionStatus.RESERVED);

		System.out.println("Transaction open at end of reserveMediaForCustomer?: "
		                   + TransactionSynchronizationManager.isActualTransactionActive());

		return mediaTransactionRepository.save(reservation);
	}

	@Override
	public List<MediaTransaction> findReservationsForUser(User user) {
		return mediaTransactionRepository.findByUserAndStatus(user, TransactionStatus.RESERVED);
	}

	@Override
	public void cancelReservation(Long reservationId) {
		if (!mediaTransactionRepository.existsById(reservationId)) {
			throw new IllegalArgumentException(String.format("Reservation with ID %d does not exist.", reservationId));
		}
		mediaTransactionRepository.deleteById(reservationId);
		System.out.printf("Reservation %d was canceled.%n", reservationId);
	}

	@Override
	public boolean canLoanReserve(User user) {
		int activeLoanReserve = mediaTransactionRepository.findActiveLoansByUser(user).size();
		return activeLoanReserve < user.getLoanLimit();
	}

	private double calculatePenalty(MediaTransaction transaction) {
		// Wenn das Rückgabedatum nach dem fälligen Rückgabedatum liegt, berechne die
		// Strafe
		if (transaction.getReturnDate() != null
		    && transaction.getReturnDate().isAfter(transaction.getLast_possible_return_date())) {
			long diffInDays = DAYS.between(transaction.getReturnDate(), transaction.getLast_possible_return_date());
			return diffInDays > 0 ? diffInDays * penaltyPerDay : 0.0; // Dynamische Gebühr pro Tag
		}
		return 0.0;
	}

	/**
	 * Find first date for media within given period when at least one edition is available.
	 */
	private LocalDate findFirstAvailableDateForMedia(Media media, LocalDate startDate, LocalDate endDate) {
		List<MediaTransaction> reservedEditions = mediaTransactionRepository.findEditionsInPeriod(media, startDate,
			endDate);

		LocalDate latestEndDate = startDate;
		for (MediaTransaction transaction : reservedEditions) {
			LocalDate transactionEndDate = transaction.getEnd_date();
			LocalDate reserveEndDate = transaction.getReserveEndDate();

			if (transactionEndDate != null && transactionEndDate.isAfter(latestEndDate)) {
				latestEndDate = transactionEndDate;
			} else if (reserveEndDate != null && reserveEndDate.isAfter(latestEndDate)) {
				latestEndDate = reserveEndDate;
			}
		}

		return latestEndDate.plusDays(1);
	}
}
