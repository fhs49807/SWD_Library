package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.model.User.CustomerType;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Transactional(timeout = 10)
	User findByUsername(String username); // -> wird user gefunden, der mit einer transaktion verknüpft ist -> MediaTransactionSerice

	// finds all customers with specific type
	@Transactional(timeout = 10)
	Collection<User> findByCustomerType(CustomerType customerType);

	// finds amount of active loans by customer
	// loans are still active if they have no return date
	@Query("SELECT COUNT(m) FROM MediaTransaction m WHERE m.user = :user AND m.return_date IS NULL")
	int countActiveLoansByUser(@Param("user") User user);

}
