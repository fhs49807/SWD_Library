package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.MediaType;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.LibraryRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LibraryService implements LibraryServiceInterface {

	private final LibraryRepository libraryRepository;
	private final GenreRepository genreRepository;
	private final MediaTypeRepository mediaTypeRepository;

	public LibraryService(LibraryRepository libraryRepository, GenreRepository genreRepository,
		MediaTypeRepository mediaTypeRepository) {
		this.libraryRepository = libraryRepository;
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
}
