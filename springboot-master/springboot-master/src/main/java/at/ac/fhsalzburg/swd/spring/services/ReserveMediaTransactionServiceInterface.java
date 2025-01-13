package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

public interface ReserveMediaTransactionServiceInterface {

	void reserveMediaForCustomer(String userName, Long mediaId, LocalDate reserveStartDate, LocalDate reserveEndDate)
		throws NotFoundException;

	ReserveMediaTransaction getLatestReservation(Long mediaId, String username);

	Collection<ReserveMediaTransaction> findReservationsForUser(User user);

	void cancelReservation(Long reservationId);
}
