package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaType;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService implements MediaServiceInterface {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MediaTypeRepository mediaTypeRepository;
    
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
    
    @Override
    public Iterable<String> getAllGenres() {
        return StreamSupport.stream(genreRepository.findAll().spliterator(), false)
                .map(Genre::getName)
                .collect(Collectors.toList()); // Return as List but Iterable-compatible
    }

    @Override
    public Iterable<String> getAllMediaTypes() {
        return StreamSupport.stream(mediaTypeRepository.findAll().spliterator(), false)
                .map(MediaType::getType)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Media> searchMediaByGenreAndType(String genre, String type) {
        return mediaRepository.findByGenreAndType(genre, type);
    }
}
