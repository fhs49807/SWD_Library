package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Price;

@Repository
public interface PriceRepository extends CrudRepository<Price, Double> {

	
	//TODO: change?
	Iterable<Price> findByAmount(double amount);

	Iterable<Price> findByCustomer(Customer customer);
}
