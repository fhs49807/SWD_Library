package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Service
public class MediaService implements MediaServiceInterface {
	
	@Autowired
    private MediaRepository mediaRepository;
	
	//TODO: Autowired
	private final GenreRepository genreRepository;

    public MediaService(GenreRepository genreRepo) {
        this.genreRepository = genreRepo;
    }

    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    public void save(Media media) {
        mediaRepository.save(media);
    }
}