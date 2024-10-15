//remove


package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;

@Repository
public interface UserRepository extends CrudRepository<Customer, String> {

	@Transactional(timeout = 10)
    Customer findByUsername(String username);

    @Transactional(timeout = 10)
    User findByEmail(String email);
}
