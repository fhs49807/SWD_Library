package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ReserveMediaTransactionService implements ReserveMediaTransactionServiceInterface {

	private final MediaServiceInterface mediaService;
	private final EditionServiceInterface editionService;
	private final ReserveMediaTransactionRepository reserveMediaTransactionRepository;
	private final UserServiceInterface userService;

	// Constructor injection for better testability
	public ReserveMediaTransactionService(MediaServiceInterface mediaService, EditionServiceInterface editionService,
			ReserveMediaTransactionRepository reserveMediaTransactionRepository, UserServiceInterface userService) {
		this.mediaService = mediaService;
		this.editionService = editionService;
		this.reserveMediaTransactionRepository = reserveMediaTransactionRepository;
		this.userService = userService;
	}

	@Override
	public ReserveMediaTransaction getLatestReservation(Long mediaId, String username) {
		return reserveMediaTransactionRepository
				.findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(mediaId, username)
				.orElseThrow(() -> new IllegalArgumentException("No reservation found for the given user and media."));
	}

	@Override
	public void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate, Date reserveEndDate)
			throws NotFoundException {
		User user = userService.getByUsername(userName);
		Media media = mediaService.findById(mediaId);

		// Check if editions are available for reservation
		List<Edition> availableEditions = editionService.findAvailableForReserve(media, reserveStartDate,
				reserveEndDate);
		if (availableEditions.isEmpty()) {
			throw new NotFoundException(String.format("No available editions for media: %s (%s) [%s]", media.getName(),
					media.getMediaType().getType(), media.getId()));
		}

		// Reserve the first available edition
		ReserveMediaTransaction reservation = new ReserveMediaTransaction(user, availableEditions.get(0),
				reserveStartDate, reserveEndDate);
		reserveMediaTransactionRepository.save(reservation);

		System.out.printf("Media %s reserved for customer %s%n", media.getId(), user.getUsername());
	}

	@Override
	public Collection<ReserveMediaTransaction> findReservationsForUser(User user) {
		return reserveMediaTransactionRepository.findByUser(user);
	}

	@Override
	public void cancelReservation(Long reservationId) {
		if (!reserveMediaTransactionRepository.existsById(reservationId)) {
			throw new IllegalArgumentException(String.format("Reservation with ID %d does not exist.", reservationId));
		}
		reserveMediaTransactionRepository.deleteById(reservationId);
		System.out.printf("Reservation %d was canceled.%n", reservationId);
	}
}
