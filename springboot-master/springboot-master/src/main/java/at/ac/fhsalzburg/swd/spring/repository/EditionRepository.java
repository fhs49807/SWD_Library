package at.ac.fhsalzburg.swd.spring.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface EditionRepository extends CrudRepository<Edition, Integer>{

	@Transactional(timeout = 10)
    Edition findById(int id);

    @Transactional
    Collection<Edition> findEditionByMedia(Media media);
}
