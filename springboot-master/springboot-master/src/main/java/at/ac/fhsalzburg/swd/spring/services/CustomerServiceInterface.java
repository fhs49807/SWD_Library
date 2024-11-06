package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;

import at.ac.fhsalzburg.swd.spring.model.Customer;

public interface CustomerServiceInterface {

	public abstract Customer save(Customer customer);

	public abstract Customer findById(Long id);

	public abstract Collection<Customer> findByName(String name);

	public abstract void deleteById(Long id);

}
