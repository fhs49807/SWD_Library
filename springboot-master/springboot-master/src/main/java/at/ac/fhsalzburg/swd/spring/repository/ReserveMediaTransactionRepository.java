package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveMediaTransactionRepository extends CrudRepository<ReserveMediaTransaction, Long> {

}
