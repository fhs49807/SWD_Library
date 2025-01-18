package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GenreCreationTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testGenreCreation() {
        // CREATE 1
        Genre scienceFiction = new Genre();
        scienceFiction.setName("Science Fiction");
        scienceFiction.setPrice(10.0);
        genreRepository.save(scienceFiction);

        // CREATE 2
        Genre fantasy = new Genre();
        fantasy.setName("Fantasy Genre");
        fantasy.setPrice(12.0);
        genreRepository.save(fantasy);

        // VERIFY
        Genre retrievedSF = genreRepository.findById(scienceFiction.getId()).orElse(null);
        Genre retrievedFantasy = genreRepository.findById(fantasy.getId()).orElse(null);

        assertNotNull(retrievedSF, "Science Fiction Genre should be created");
        assertNotNull(retrievedFantasy, "Fantasy Genre should be created");

        assertEquals("Science Fiction", retrievedSF.getName());
        assertEquals(10.0, retrievedSF.getPrice());
        assertEquals("Fantasy Genre", retrievedFantasy.getName());
        assertEquals(12.0, retrievedFantasy.getPrice());
    }
}
