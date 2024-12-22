package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaType;

import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.LibraryRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LibraryService implements LibraryServiceInterface {

	private final LibraryRepository libraryRepository;
	private final MediaServiceInterface mediaService;
	private final EditionServiceInterface editionService;
	private final GenreRepository genreRepository;
	private final MediaTypeRepository mediaTypeRepository;

	public LibraryService(LibraryRepository libraryRepository, MediaServiceInterface mediaService,
			EditionServiceInterface editionService, GenreRepository genreRepository,
			MediaTypeRepository mediaTypeRepository) {
		this.libraryRepository = libraryRepository;
		this.mediaService = mediaService;
		this.editionService = editionService;
		this.genreRepository = genreRepository;
		this.mediaTypeRepository = mediaTypeRepository;
	}

	@Override
	public Library save(Library library) {
		return libraryRepository.save(library);
	}

	@Override
	public List<String> getAllGenres() {
		return StreamSupport.stream(genreRepository.findAll().spliterator(), false).map(Genre::getName)
				.collect(Collectors.toList());
	}

	@Override
	public List<String> getAllMediaTypes() {
		return StreamSupport.stream(mediaTypeRepository.findAll().spliterator(), false).map(MediaType::getType)
				.collect(Collectors.toList());
	}

	@Override
	public Library findById(Long id) {
		return libraryRepository.findById(id).orElse(null);
	}

	@Override
	public List<Library> findByName(String name) {
		return libraryRepository.findByName(name);
	}

	@Override
	public void deleteById(Long id) {
		libraryRepository.deleteById(id);
	}

	@Override
	public Collection<Media> validateMedia(String mediaId) {
		return mediaService.findMediaById(Long.parseLong(mediaId));
	}

	@Override
	public Collection<Edition> checkForAvailableEditions(Media media) {
		return editionService.findByMediaAndAvailable(media);
	}

	@Override
	public Edition findAvailableEdition(Collection<Edition> editions) {
		return editionService.findFirstAvailableEdition(editions);
	}

	@Override
	public boolean isEditionAvailable(Edition edition) {
		return editionService.isEditionAvailable(edition);
	}

}
