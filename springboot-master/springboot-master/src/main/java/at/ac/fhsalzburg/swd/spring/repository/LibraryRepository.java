package at.ac.fhsalzburg.swd.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Library;

@Repository
public interface LibraryRepository extends CrudRepository<Library, Long> {

	@Transactional(timeout = 10)
	List<Library> findByName(String name);

}
