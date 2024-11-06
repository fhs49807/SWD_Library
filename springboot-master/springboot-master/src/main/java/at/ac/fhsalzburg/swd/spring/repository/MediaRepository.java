package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaType;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {

	@Transactional(timeout = 10)
	Media findByName(String name);

	@Transactional(timeout = 10)
	Iterable<Media> findByAvailabilityStatus(String availabilityStatus);

	@Transactional(timeout = 10)
	Iterable<Media> findByMediaType(MediaType mediaType);
}