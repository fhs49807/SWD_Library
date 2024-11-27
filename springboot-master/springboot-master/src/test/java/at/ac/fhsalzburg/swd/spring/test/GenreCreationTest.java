package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional // sorgt dafür, dass alle Änderungen in der DB nach jedem Test zurückgesetzt werden
public class GenreCreationTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testGenreCreation() {
        // Erstellt und speichert ein Genre
        Genre scienceFiction = new Genre();
        scienceFiction.setName("Science Fiction");
        scienceFiction.setPrice(10.0);
        genreRepository.save(scienceFiction);

        Genre fantasy = new Genre();
        fantasy.setName("Fantasy Genre");
        fantasy.setPrice(10.0);
        genreRepository.save(fantasy);

        // Überprüft, ob die Genres erfolgreich gespeichert wurden
        Genre retrievedScienceFiction = genreRepository.findById(scienceFiction.getId()).orElse(null);
        Genre retrievedFantasy = genreRepository.findById(fantasy.getId()).orElse(null);

        assertNotNull(retrievedScienceFiction, "Science Fiction Genre should be created");
        assertNotNull(retrievedFantasy, "Fantasy Genre should be created");

        // Überprüft die Details der Genres
        assertEquals("Science Fiction", retrievedScienceFiction.getName());
        assertEquals(10.0, retrievedScienceFiction.getPrice());
        assertEquals("Fantasy Genre", retrievedFantasy.getName());
        assertEquals(10.0, retrievedFantasy.getPrice());
    }
}

