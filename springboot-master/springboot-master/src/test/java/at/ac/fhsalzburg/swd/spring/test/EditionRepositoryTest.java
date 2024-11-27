package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Book;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class EditionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EditionRepository editionRepository;

    @Test
    public void whenFindByMedia_thenReturnEditions() {
        // given
        Book book = new Book();
        book.setName("JAVA 101");
        book.setISBN("isbn123");
        entityManager.persist(book);
        entityManager.flush();

        Edition givenEdition = new Edition(book);
        entityManager.persist(givenEdition);
        entityManager.flush();

        List<Edition> foundEditions = editionRepository.findByMedia(book);

        // Validate that the retrieved edition matches the given edition
        assertEquals(1, foundEditions.size());
        assertEquals(givenEdition, foundEditions.get(0));
    }

}
