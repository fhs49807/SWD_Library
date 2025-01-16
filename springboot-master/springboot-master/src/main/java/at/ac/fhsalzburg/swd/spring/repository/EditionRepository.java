package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EditionRepository extends CrudRepository<Edition, Long> {

	// finds all editions by specific media item
	@Transactional(timeout = 10)
	List<Edition> findByMedia(Media media);

	@Query("SELECT e " +
	       "FROM Edition e join MediaTransaction t on t.edition = e " +
	       "WHERE e.media = :media" +
	       "    and t.status = 'LOANED'" +
	       "    and t.return_date is null" +
	       "    and t.end_date >= :startDate")
	List<Edition> findLoanedEditions(Media media, LocalDate startDate);

	@Query("SELECT e " +
	       "FROM Edition e join MediaTransaction t on t.edition = e " +
	       "WHERE e.media = :media" +
	       "    and t.status = 'RESERVED'" +
	       "    and t.return_date is null" +
	       "    and t.reserveStartDate <= :endDate" +
	       "    and t.reserveEndDate >= :startDate")
	List<Edition> findReservedEditions(Media media, LocalDate startDate, LocalDate endDate);


	@Query("SELECT e.id FROM Media m JOIN m.editions e WHERE m.id = :mediaId")
	List<Long> findEditionIdsByMediaId(@Param("mediaId") Long mediaId);

}
