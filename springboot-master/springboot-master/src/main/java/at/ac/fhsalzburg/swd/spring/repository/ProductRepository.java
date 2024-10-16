package at.ac.fhsalzburg.swd.spring.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

	@Transactional(timeout = 10)
    List<Product> findByName(String name);

	@Transactional(timeout = 10)
	Product findById(long id);

}
