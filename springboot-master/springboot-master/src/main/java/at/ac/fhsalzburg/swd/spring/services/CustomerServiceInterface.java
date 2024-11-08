package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;

import at.ac.fhsalzburg.swd.spring.model.Customer;

public interface CustomerServiceInterface {

	// create/ update customer
	public abstract Customer save(Customer customer);

	// retrieve customer by id
	public abstract Customer findById(Long id);

	// search for customer by name
	public abstract Collection<Customer> findByName(String name);

	// delete customer by id
	public abstract void deleteById(Long id);

	// retrieve age of customer
	public abstract int getAge(Customer customer);

	// check if customer has reached loan limit
	public abstract boolean canLoanMore(Customer customer);

	// check if customer has outstanding fees
	public abstract boolean hasOutstandingFees(Customer customer);

}
