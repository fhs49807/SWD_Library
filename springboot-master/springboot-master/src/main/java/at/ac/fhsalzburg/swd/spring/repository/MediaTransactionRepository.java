package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaTransactionRepository extends CrudRepository{

	//TODO: @Transactional
	//TODO: @Query
	
}
