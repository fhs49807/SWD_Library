package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;

@Repository
public interface LibraryRepository extends CrudRepository<MediaTransaction, Long> {

	// TODO: @Transactional
	// TODO: @Query

}
