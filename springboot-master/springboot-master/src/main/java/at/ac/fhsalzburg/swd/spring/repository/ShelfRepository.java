package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Shelf;

@Repository
public interface ShelfRepository extends CrudRepository<Shelf, Long> {

	@Transactional(timeout = 10)
	Shelf findById(int id);

}
