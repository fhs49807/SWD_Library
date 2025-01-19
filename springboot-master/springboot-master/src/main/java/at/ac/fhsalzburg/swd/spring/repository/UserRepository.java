package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	// -> wird user gefunden, der mit einer transaktion verknüpft ist -> MediaTransactionSerice
	@Transactional(timeout = 10)
	User findByUsername(String username);
}
