package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReserveMediaTransactionRepository extends CrudRepository<ReserveMediaTransaction, Long> {

	Optional<ReserveMediaTransaction> findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(Long mediaId,
		String username);

	Collection<ReserveMediaTransaction> findByUser(User user);

	@Query("SELECT t " +
	       "FROM ReserveMediaTransaction t JOIN Edition e ON e = t.edition " +
	       "WHERE e.media = :media AND (t.reserveStartDate <= :endDate OR t.reserveEndDate >= :startDate) " +
	       "ORDER BY t.reserveEndDate")
	List<ReserveMediaTransaction> findReservedEditionsInPeriod(Media media, LocalDate startDate,
		LocalDate endDate);
}
