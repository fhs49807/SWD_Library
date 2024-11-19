package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Audio;
import at.ac.fhsalzburg.swd.spring.model.Book;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaType;
import at.ac.fhsalzburg.swd.spring.model.Movie;
import at.ac.fhsalzburg.swd.spring.repository.AudioRepository;
import at.ac.fhsalzburg.swd.spring.repository.BookRepository;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;
import at.ac.fhsalzburg.swd.spring.repository.MovieRepository;

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
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired    
    private EditionRepository editionRepository;
    
    // constructor for injection of GenreRepo instance
    public MediaService(GenreRepository genreRepo) {
        this.genreRepository = genreRepo;
    }

    @Override
    public Boolean addMedia(Media media) {
        if ((media.getName() != null) && (media.getName().length() > 0) && (media.getFSK() >= 0) && (media.getFSK() <= 18)) {
            // Save media in the main media repository
            mediaRepository.save(media);

            // Handle specific types of media
            if (media instanceof Book) {
                bookRepository.save((Book) media);
            } else if (media instanceof Audio) {
                audioRepository.save((Audio) media);
            } else if (media instanceof Movie) {
            	movieRepository.save((Movie) media);
            }

            // Add 2 exemplare for each medium
            for (int i = 0; i < 2; i++) {
                Edition edition = new Edition(media);
                editionRepository.save(edition);
            }

            return true; // Media successfully added
        }

        return false; // Media validation failed
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
