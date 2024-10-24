package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer>{

	@Transactional(timeout = 10)
    Customer findByUsername(String username);
}
