package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;

public interface InvoiceServiceInterface {

	// deduct outstanding amount
	public abstract void deductAmount(User user, MediaTransaction transaction);
	
	// get outstanding balance from customer
	public abstract double getOutstandingBalance(User user);

}
