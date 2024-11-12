package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Customer.CustomerType;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	// finds all customers with specific name
	@Transactional(timeout = 10)
	Customer findByName(String name);

	// finds all customers with specific type
	@Transactional(timeout = 10)
	Collection<Customer> findByCustomerType(CustomerType customerType);

	// finds amount of active loans by customer
	// loans are still active if they have no return date
	@Query("SELECT COUNT(m) FROM MediaTransaction m WHERE m.customer = :customer AND m.returnDate IS NULL")
	int countActiveLoansByCustomer(@Param("customer") Customer customer);

}
