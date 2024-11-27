package at.ac.fhsalzburg.swd.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

	@Transactional(timeout = 10)
	Media findByName(String name);

	@Query("SELECT m FROM Media m WHERE m.genre.name = :genre AND m.mediaType.typeName = :typeName")
	List<Media> findByGenreAndType(@Param("genre") String genre, @Param("typeName") String typeName);

	@Query("SELECT e.id FROM Media m JOIN m.editions e WHERE m.id = :mediaId")
	List<Long> findEditionIdsByMediaId(@Param("mediaId") Long mediaId);

}