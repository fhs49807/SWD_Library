package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Book;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{

	@Transactional(timeout = 10)
    Media findById(int id);
	
}
