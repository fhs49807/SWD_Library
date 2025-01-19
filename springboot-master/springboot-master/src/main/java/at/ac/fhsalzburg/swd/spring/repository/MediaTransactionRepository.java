package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaTransactionRepository extends CrudRepository<MediaTransaction, Long> {

	@Transactional(timeout = 10)
	List<MediaTransaction> findByUserAndStatus(User user, TransactionStatus status);

	Optional<MediaTransaction> findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(Long mediaId,
		String username);

	@Query("SELECT t " +
	       "FROM MediaTransaction t JOIN Edition e ON e = t.edition " +
	       "WHERE e.media = :media" +
	       "    AND (t.reserveStartDate <= :endDate OR t.reserveEndDate >= :startDate" +
	       "    OR t.start_date <= :endDate OR t.end_date >= :startDate)")
	List<MediaTransaction> findEditionsInPeriod(Media media, LocalDate startDate, LocalDate endDate);

	@Query("SELECT t FROM MediaTransaction t WHERE t.user = :user AND t.reserveStartDate IS NULL")
	List<MediaTransaction> findActiveLoansByUser(User user);
}
