package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.User;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MediaTransactionRepository extends CrudRepository<MediaTransaction, Long> {

	@Transactional(timeout = 10)
	Optional<MediaTransaction> findById(long id); //findet die spezifische transaktion basierend auf der ID -> MediaTransactionService mediaTransactionRepository.save(transaction)

	@Transactional(timeout = 10)
    Collection<MediaTransaction> findByEdition(Edition edition);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByUser(User user);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByStatus(TransactionStatus status);

    boolean existsByUserAndEditionAndStatus(User user, Edition edition, MediaTransaction.TransactionStatus status);

}
