package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaTransactionRepository extends CrudRepository<MediaTransaction, Long> {

	//findet die spezifische transaktion basierend auf der ID
	// -> MediaTransactionService mediaTransactionRepository.save(transaction)
	@Transactional(timeout = 10)
	Optional<MediaTransaction> findById(long id);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByEdition(Edition edition);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByUser(User user);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByStatus(TransactionStatus status);

	@Transactional(timeout = 10)
	List<MediaTransaction> findByUserAndStatus(User user, TransactionStatus status);

	boolean existsByUserAndEditionAndStatus(User user, Edition edition, MediaTransaction.TransactionStatus status);

	Optional<MediaTransaction> findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(Long mediaId,
		String username);

	@Query("SELECT t " +
	       "FROM MediaTransaction t JOIN Edition e ON e = t.edition " +
	       "WHERE e.media = :media AND (t.reserveStartDate <= :endDate OR t.reserveEndDate >= :startDate" +
	       "    OR t.start_date <= :endDate OR t.end_date >= :startDate) " +
	       "ORDER BY t.reserveEndDate")
	List<MediaTransaction> findReservedEditionsInPeriod(Media media, LocalDate startDate, LocalDate endDate);

}
