package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Mediatype;

@Repository
public interface MediaTypeRepository extends CrudRepository<Mediatype, Integer> {

    @Transactional(timeout = 10)
    Mediatype findById(int id);

    Mediatype findByType(String type);
}