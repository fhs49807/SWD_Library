package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {

    @Transactional(timeout = 10)
    Genre findById(long id);

    Genre findByName(String n);
}