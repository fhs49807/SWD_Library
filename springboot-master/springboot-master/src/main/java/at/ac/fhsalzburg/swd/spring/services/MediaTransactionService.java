package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
		return mediaTransactionRepository.findByUserAndStatus(user, MediaTransaction.TransactionStatus.LOANED);
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
	public MediaTransaction loanMedia(String username, Long mediaId, LocalDate endDate) {
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
			handleNoAvailableEditionError(media, LocalDate.now(), endDate);
		}
		Edition edition = availableEditions.get(0);

		// Calculate return dates
		User user = userService.getByUsername(username);
		int maxLoanDays = user.getCustomerType() == User.CustomerType.STUDENT ? 42 : 28;
		LocalDate lastPossibleReturnDate = todayDate.plusDays(maxLoanDays);

		// Create and save transaction
		MediaTransaction transaction =
			new MediaTransaction(LocalDate.now(), endDate, lastPossibleReturnDate, edition, user, null, null,
				MediaTransaction.TransactionStatus.LOANED);
		mediaTransactionRepository.save(transaction);

		// Update edition
		editionService.markEditionAsUnavailable(edition, transaction.getEnd_date());

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
		transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

		// Mache die Edition wieder verfügbar
		Edition edition = transaction.getEdition();
		edition.setAvailable(true); // Die Edition wird wieder verfügbar

		// Das ORM kümmert sich um das Speichern der Änderungen
	}

	@Override
	public MediaTransaction getLatestReservation(Long mediaId, String username) {
		return mediaTransactionRepository.findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(mediaId, username)
			.orElseThrow(() -> new IllegalArgumentException("No reservation found for the given user and media."));
	}

	@Override
	public MediaTransaction reserveMediaForCustomer(String userName, Long mediaId, LocalDate reserveStartDate,
		LocalDate reserveEndDate) throws IllegalStateException, NoSuchElementException {
		User user = userService.getByUsername(userName);
		Media media = mediaService.findById(mediaId);

		// Check if editions are available for reservation
		List<Edition> availableEditions = editionService.findAvailableEditions(media, reserveStartDate,
			reserveEndDate);
		if (availableEditions.isEmpty()) {
			handleNoAvailableEditionError(media, reserveStartDate, reserveEndDate);
		}

		// Reserve the first available edition
		int maxLoanDays = user.getCustomerType() == User.CustomerType.STUDENT ? 42 : 28;
		LocalDate lastPossibleReturnDate = reserveStartDate.plusDays(maxLoanDays);

		MediaTransaction reservation =
			new MediaTransaction(null, null, lastPossibleReturnDate, availableEditions.get(0), user,
				reserveStartDate, reserveEndDate, MediaTransaction.TransactionStatus.RESERVED);
		return mediaTransactionRepository.save(reservation);
	}

	@Override
	public List<MediaTransaction> findReservationsForUser(User user) {
		return mediaTransactionRepository.findByUserAndStatus(user, MediaTransaction.TransactionStatus.RESERVED);
	}

	@Override
	public void cancelReservation(Long reservationId) {
		if (!mediaTransactionRepository.existsById(reservationId)) {
			throw new IllegalArgumentException(String.format("Reservation with ID %d does not exist.", reservationId));
		}
		mediaTransactionRepository.deleteById(reservationId);
		System.out.printf("Reservation %d was canceled.%n", reservationId);
	}

	private double calculatePenalty(MediaTransaction transaction) {
		// Wenn das Rückgabedatum nach dem fälligen Rückgabedatum liegt, berechne die Strafe
		if (transaction.getReturnDate() != null &&
		    transaction.getReturnDate().isAfter(transaction.getLast_possible_return_date())) {
			long diffInDays = DAYS.between(transaction.getReturnDate(), transaction.getLast_possible_return_date());
			return diffInDays > 0 ? diffInDays * penaltyPerDay : 0.0; // Dynamische Gebühr pro Tag
		}
		return 0.0;
	}

	private void handleNoAvailableEditionError(Media media, LocalDate startDate,
		LocalDate endDate) throws IllegalStateException, NoSuchElementException {
		// find first available date for given period, so that an edition of the given media can be reserved
		List<MediaTransaction> reservedEditions =
			mediaTransactionRepository.findReservedEditionsInPeriod(media, startDate, endDate);
		MediaTransaction transaction = reservedEditions.get(0);
		LocalDate endDateOfEdition;
		if (MediaTransaction.TransactionStatus.LOANED == transaction.getStatus()) {
			endDateOfEdition = transaction.getEnd_date();
		} else if (MediaTransaction.TransactionStatus.RESERVED == transaction.getStatus()) {
			endDateOfEdition = transaction.getReserveEndDate();
		} else {
			throw new IllegalStateException(
				String.format("Transaction %d has undefined status %s", transaction.getId(),
					transaction.getStatus()));
		}

		throw new NoSuchElementException(
			String.format(
				"No available editions for media: %s (%s). Please select another time period. The first possible" +
				" start date would be on %s", media.getName(), media.getMediaType().getType(),
				endDateOfEdition.plusDays(1)));
	}
}
