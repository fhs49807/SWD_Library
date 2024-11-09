package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;

public interface MediaTransactionServiceInterface {

	// create loan record
	public abstract MediaTransaction createLoanRecord(Customer customer, Date dueDate, Collection<Edition> editions);

	// retrieve all loans associated to custmer
	public abstract Collection<MediaTransaction> findLoansByUser(Customer customer);

	// retrieve all current loans
	public abstract Collection<MediaTransaction> getAllLoans();

	public abstract void returnMedia(Long transactionId);
}
