package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Library;

public interface LibraryServiceInterface {

	Library save(Library library);

	Library findById(Long id);

	Iterable<String> getAllGenres();

	Iterable<String> getAllMediaTypes();
}
