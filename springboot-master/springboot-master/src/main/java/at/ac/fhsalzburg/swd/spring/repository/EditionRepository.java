package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface EditionRepository extends CrudRepository<Edition, Long>{

	@Transactional(timeout = 10)
    Edition findById(long id);

	@Transactional(timeout = 10)
    Collection<Edition> findEditionByMedia(Media media);
}
