package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.model.Genre;

@Service
public class MediaService implements MediaServiceInterface {
	@SuppressWarnings("unused")
	@Autowired
    private MediaRepository mediaRepository;
	
    private final GenreRepository genreRepository;

    public MediaService(GenreRepository genreRepo) {
        this.genreRepository = genreRepo;
    }

    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }
}