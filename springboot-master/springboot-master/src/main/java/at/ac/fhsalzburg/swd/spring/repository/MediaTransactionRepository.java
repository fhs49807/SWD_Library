package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction.TransactionStatus;

import java.util.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MediaTransactionRepository extends CrudRepository<MediaTransaction, Long> {

	@Transactional(timeout = 10)
	MediaTransaction findById(long id);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByEdition(Edition edition);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByCustomer(Customer customer);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByStatus(TransactionStatus status);

	// TODO: find transactions by date??
}
