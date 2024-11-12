package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService implements MediaServiceInterface {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private GenreRepository genreRepository;

    // constructor for injection of GenreRepo instance
    public MediaService(GenreRepository genreRepo) {
        this.genreRepository = genreRepo;
    }

    // saves genre entry to repository
    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    // saves media to repository
    public void save(Media media) {
        mediaRepository.save(media);
    }

    public Media findById(Long id) {
        return mediaRepository.findById(id).orElseThrow();
    }
}
