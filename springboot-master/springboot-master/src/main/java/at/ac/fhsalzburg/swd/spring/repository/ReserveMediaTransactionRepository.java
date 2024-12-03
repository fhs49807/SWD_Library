package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveMediaTransactionRepository extends CrudRepository<ReserveMediaTransaction, Long> {

	Optional<ReserveMediaTransaction> findTopByEdition_Media_IdAndUser_UsernameOrderByIdDesc(Long mediaId,
			String username);

}
