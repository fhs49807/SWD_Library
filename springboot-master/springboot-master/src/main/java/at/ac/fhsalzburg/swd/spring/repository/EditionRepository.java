package at.ac.fhsalzburg.swd.spring.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface EditionRepository extends CrudRepository<Edition, Long> {

	// finds all editions by specific media item
	@Transactional(timeout = 10)
	List<Edition> findByMedia(Media media);

	// checks if edition with specific ID is available
	boolean existsByIdAndAvailable(Long editionId, boolean available);

	// finds all available editions associated with specific media item
	@Query("SELECT e FROM Edition e WHERE e.media = :media AND e.available = true")
	List<Edition> findByMediaAndAvailable(@Param("media") Media media);

    @Query("SELECT e " +
           "FROM Edition e left join ReserveMediaTransaction t on t.edition = e " +
           "WHERE e.media = :media " +
           "AND (t is null OR t.reserveStartDate > :endDate or t.reserveEndDate < :startDate)")
    List<Edition> findAvailableForReserve(Media media, LocalDate startDate, LocalDate endDate);

    @Query("SELECT e.id FROM Media m JOIN m.editions e WHERE m.id = :mediaId")
	List<Long> findEditionIdsByMediaId(@Param("mediaId") Long mediaId);

}
