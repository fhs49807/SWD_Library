package at.ac.fhsalzburg.swd.spring.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.ReturnMedia;

@Repository
public interface ReturnMediaRepository extends CrudRepository<ReturnMedia, Integer> {

	List<ReturnMedia> findByCustomer(Customer customer);

	List<ReturnMedia> findByReturnDate(Date returnDate);

}
