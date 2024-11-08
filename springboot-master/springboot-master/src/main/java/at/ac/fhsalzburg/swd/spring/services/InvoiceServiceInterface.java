package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Media;

public interface InvoiceServiceInterface {

	// deduct outstanding amount
	public abstract void deductAmount(Customer customer, Media media);
	
	// get outstanding balance from customer
	public abstract double getOutstandingBalance(Customer customer);

}
