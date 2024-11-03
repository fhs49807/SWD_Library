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
    public void whenFindByName_thenReturnGenre() {
        // given
        Media media = new Book(123, "available", null, "JAVA 101", null, null, "isbn123");
        entityManager.persist(media);
        entityManager.flush();

        Edition givenEdition = new Edition(media);
        entityManager.persist(givenEdition);
        entityManager.flush();

        // when
        List<Edition> foundEditions = editionRepository.findEditionByMedia(media);

        // then
        assertEquals(givenEdition, foundEditions.get(0));
    }
}
