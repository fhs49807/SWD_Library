package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Audio;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface AudioRepository extends CrudRepository<Audio, Integer> {

	@Transactional(timeout = 10)
	Media findById(int id);

}
