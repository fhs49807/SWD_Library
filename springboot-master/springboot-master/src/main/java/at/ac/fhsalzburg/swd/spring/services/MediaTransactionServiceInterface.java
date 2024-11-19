package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;

public interface MediaTransactionServiceInterface {

	// create loan record
	public Collection<MediaTransaction> createLoanRecord(User user, Date dueDate, Collection<Edition> editions);

	// retrieve all loans associated to custmer
	public abstract Collection<MediaTransaction> findLoansByUser(User user);

	// retrieve all current loans
	public abstract Collection<MediaTransaction> getAllLoans();

	// Mario Neubacher
	public abstract void returnMedia(Long transactionId);

	// Thomas Clermont
	public abstract MediaTransaction loanMedia(String username, Collection<Long> editionIds, Date dueDate);
}
