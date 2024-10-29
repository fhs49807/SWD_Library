package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaTransactionRepository extends CrudRepository<MediaTransaction, Integer> {

	//TODO: @Transactional
	//TODO: @Query

}
