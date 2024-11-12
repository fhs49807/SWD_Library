package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;
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
}
