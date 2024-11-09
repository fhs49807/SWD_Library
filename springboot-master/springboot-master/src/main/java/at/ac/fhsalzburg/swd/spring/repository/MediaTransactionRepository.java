package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction.TransactionStatus;

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
	Optional<MediaTransaction> findById(long id);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByEditions(Edition edition);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByCustomer(Customer customer);

	@Transactional(timeout = 10)
	Collection<MediaTransaction> findByStatus(TransactionStatus status);

	// get all reservations between startDate and endDate
	// used in handleReservations in LibraryService to determine availability of edition for loan period
	@Query("SELECT m FROM MediaTransaction m WHERE :media MEMBER OF m.media AND m.expirationDate BETWEEN :startDate AND :endDate")
	Collection<MediaTransaction> findReservationsByDateRange(@Param("media") Media media,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
