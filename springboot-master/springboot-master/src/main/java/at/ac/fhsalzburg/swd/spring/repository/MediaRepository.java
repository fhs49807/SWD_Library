package at.ac.fhsalzburg.swd.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

	Media findByName(String name);

	@Query("SELECT m FROM Media m WHERE m.mediaType.typeName = :typeName")
	List<Media> findByType(@Param("typeName") String typeName);

	@Query("SELECT m FROM Media m WHERE m.genre.name = :genreName")
	List<Media> findByGenre(@Param("genreName") String genreName);

	@Query("SELECT m FROM Media m WHERE (:genre IS NULL OR m.genre.name = :genre) AND (:typeName IS NULL OR m.mediaType.typeName = :typeName)")
	List<Media> findByGenreAndTypeOptional(@Param("genre") String genre, @Param("typeName") String typeName);

	@Query("SELECT m FROM Media m WHERE (:genre IS NULL OR m.genre.name = :genre) AND (:typeName IS NULL OR m.mediaType.typeName = :typeName) AND m.FSK <= :userAge")
	List<Media> findByGenreAndTypeAndFSK(@Param("genre") String genre, @Param("typeName") String typeName,
			@Param("userAge") int userAge);

	@Query("SELECT e.id FROM Media m JOIN m.editions e WHERE m.id = :mediaId")
	List<Long> findEditionIdsByMediaId(@Param("mediaId") Long mediaId);
}
