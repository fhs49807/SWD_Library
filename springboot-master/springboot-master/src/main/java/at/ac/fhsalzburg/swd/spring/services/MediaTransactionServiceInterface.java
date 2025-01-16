package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface MediaTransactionServiceInterface {

	Collection<MediaTransaction> findLoansByUser(User user);

	Collection<MediaTransaction> getAllLoans();

	void returnMedia(Long transactionId);

	MediaTransaction loanMedia(String username, Long mediaId, LocalDate dueDate);

	MediaTransaction findById(Long transactionId);

	MediaTransaction reserveMediaForCustomer(String userName, Long mediaId, LocalDate reserveStartDate,
		LocalDate reserveEndDate) throws IllegalStateException, NotFoundException;

	MediaTransaction getLatestReservation(Long mediaId, String username);

	List<MediaTransaction> findReservationsForUser(User user);

	void cancelReservation(Long reservationId);
}
