package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ReserveMediaTransactionService implements ReserveMediaTransactionServiceInterface {

	@Autowired
	private MediaServiceInterface mediaService;

	@Autowired
	private EditionServiceInterface editionService;

	@Autowired
	private ReserveMediaTransactionRepository reserveMediaTransactionRepository;

	@Autowired
	private UserServiceInterface userService;

	@Override
	public ReserveMediaTransaction getLatestReservation(Long mediaId, String username) {
		return reserveMediaTransactionRepository.findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(mediaId,
			username).orElseThrow(() -> new IllegalArgumentException("No reservation found for user and media."));
	}


	@Override
	public void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate,
		Date reserveEndDate) throws NotFoundException {
		User user = userService.getByUsername(userName);
		Media media = mediaService.findById(mediaId);

		// check if media is available
		List<Edition> editions = editionService.findAvailableForReserve(media, reserveStartDate, reserveEndDate);
		if (editions.isEmpty()) {
			throw new NotFoundException(
				"no more editions of media: " + media.getId() + "|" + media.getMediaType() + "|" + media.getName() +
				" left for reservations");
		} else {
			// media is available => reserve media for customer
			ReserveMediaTransaction reserveMediaTransaction =
				new ReserveMediaTransaction(user, editions.get(0), reserveStartDate, reserveEndDate);
			reserveMediaTransactionRepository.save(reserveMediaTransaction);

			System.out.println("media " + media.getId() + " was reserved for customer " + user.getUsername());
		}
	}

	@Override
	public Collection<ReserveMediaTransaction> findReservationsForUser(User user) {
		return reserveMediaTransactionRepository.findByUser(user);
	}

}
