package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.Library;

@Repository
public interface LibraryRepository extends CrudRepository<Library, Integer> {

    Library findByCellAndShelf(int cell, int shelf);

}
