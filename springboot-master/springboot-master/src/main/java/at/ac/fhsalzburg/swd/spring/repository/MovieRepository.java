package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    @Transactional(timeout = 10)
    Media findById(int id);
}

