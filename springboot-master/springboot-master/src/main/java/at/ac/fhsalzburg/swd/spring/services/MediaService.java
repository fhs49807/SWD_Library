package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.model.Genre;

@Service
public class MediaService implements MediaServiceInterface {
    private final GenreRepository genreRepository;

    public MediaService(GenreRepository genreRepo) {
        this.genreRepository = genreRepo;
    }

    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }
}