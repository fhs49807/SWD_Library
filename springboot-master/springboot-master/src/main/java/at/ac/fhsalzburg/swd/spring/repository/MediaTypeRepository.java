package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.MediaType;

@Repository
public interface MediaTypeRepository extends CrudRepository<MediaType, Long> {

    @Transactional(timeout = 10)
    MediaType findById(int id);

    MediaType findByType(String type);
}