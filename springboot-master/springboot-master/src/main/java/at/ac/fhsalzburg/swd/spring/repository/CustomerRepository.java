package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Customer.CustomerType;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Transactional(timeout = 10)
	Collection<Customer> findByName(String name); // Return multiple customers by name

	@Transactional(timeout = 10)
	Collection<Customer> findByCustomerType(CustomerType customerType);

}
