package at.ac.fhsalzburg.swd.spring.test;

import static org.junit.Assert.assertEquals;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnCustomer() {
        // given
        User givenUser = new User("Max", "Max Mustermann", "max@muster.com", "123", new Date(),"","USER",null);
        entityManager.persist(givenUser);
        entityManager.flush();

        // when
        User foundUser = userRepository.findByUsername(givenUser.getUsername());

        // then
        assertEquals(givenUser, foundUser);

    }

}
