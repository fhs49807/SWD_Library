package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

	@Autowired
	private UserService userService;

	// constructor for injection of GenreRepo instance
	public MediaService(GenreRepository genreRepo) {
		this.genreRepository = genreRepo;
	}

	@Override
	public Boolean addMedia(Media media) {
		if ((media.getName() != null) && (media.getName().length() > 0) && (media.getFSK() >= 0)
				&& (media.getFSK() <= 18)) {
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

			// Add 2 exemplare (editions) for each medium
			for (int i = 0; i < 2; i++) {
				Edition edition = new Edition(media);
				edition.setMediaName(media.getName()); // Set the media name for each Edition
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

	public List<Long> getEditionIdsByMediaId(Long mediaId) {
		List<Long> editionIds = mediaRepository.findEditionIdsByMediaId(mediaId);
		System.out.println("Edition IDs for Media ID " + mediaId + ": " + editionIds);
		return editionIds;
	}

	@Override
	public Iterable<String> getAllGenres() {
		return StreamSupport.stream(genreRepository.findAll().spliterator(), false).map(Genre::getName)
				.collect(Collectors.toList()); // Return as List but Iterable-compatible
	}

	@Override
	public Iterable<String> getAllMediaTypes() {
		return StreamSupport.stream(mediaTypeRepository.findAll().spliterator(), false).map(MediaType::getType)
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<Media> searchMediaByGenreAndType(String genre, String type, User user) {
		// Get the user's age using UserService
		int userAge = userService.getAge(user);

		// Use repository method with null-safe logic
		List<Media> mediaList = mediaRepository.findByGenreAndTypeOptional(isAll(genre) ? null : genre,
				isAll(type) ? null : type);

		// Filter media by FSK restriction
		return mediaList.stream().filter(media -> media.getFSK() <= userAge).collect(Collectors.toList());
	}

	// Helper method to check if "All" or empty
	private boolean isAll(String value) {
		return value == null || value.isEmpty() || value.equalsIgnoreCase("All Genres")
				|| value.equalsIgnoreCase("All Media Types");
	}

	@Override
	public Media findByName(String name) {
		return mediaRepository.findByName(name);
	}
}
