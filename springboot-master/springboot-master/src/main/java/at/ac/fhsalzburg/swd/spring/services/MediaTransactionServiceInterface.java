package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;

import java.util.Collection;
import java.util.Date;

public interface MediaTransactionServiceInterface {

	Collection<MediaTransaction> findLoansByUser(User user);

	Collection<MediaTransaction> getAllLoans();

	void returnMedia(Long transactionId);

	MediaTransaction loanMedia(String username, Long mediaId, Date dueDate);

	MediaTransaction findById(Long transactionId);
}
