package at.ac.fhsalzburg.swd.spring.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.repository.InvoiceRepository;

@Service
public class CustomerService implements CustomerServiceInterface {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	// save customer to repository
	@Override
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	// find customer by id
	@Override
	public Customer findById(Long id) {
		return customerRepository.findById(id).orElse(null);
	}

	// find customer by name
	@Override
	public Collection<Customer> findByName(String name) {
		return customerRepository.findByName(name);
	}

	// delete customer by id
	@Override
	public void deleteById(Long id) {
		customerRepository.deleteById(id);
	}

	// calculate age of customer based on their birth date
	@Override
	public int getAge(Customer customer) {
		LocalDate birthDate = customer.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return Period.between(birthDate, LocalDate.now()).getYears();
	}

	// checks if a customer can loan more based on their loanLimit
	@Override
	public boolean canLoanMore(Customer customer) {
		int currentLoans = customerRepository.countActiveLoansByCustomer(customer);
		return currentLoans < customer.getLoanLimit();
	}

	// checks if customer has any outstanding fees
	@Override
	public boolean hasOutstandingFees(Customer customer) {
		Double outstandingBalance = invoiceRepository.calculateOutstandingBalance(customer);
		return outstandingBalance != null && outstandingBalance > 0;
	}

}