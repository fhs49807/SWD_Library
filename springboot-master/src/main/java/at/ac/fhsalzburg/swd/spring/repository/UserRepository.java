package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Transactional(timeout = 10)
    User findByUsername(String username);

    @Transactional(timeout = 10)
    User findByEmail(String email);
}
