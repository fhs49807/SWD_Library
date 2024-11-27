package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Invoice;
import at.ac.fhsalzburg.swd.spring.model.User;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

	@Transactional(timeout = 10)
	Invoice findById(int id);

	// get outstanding balance for specific customer
	@Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.user = :user AND i.paymentStatus = false")
	Double calculateOutstandingBalance(@Param("user") User user);

}
