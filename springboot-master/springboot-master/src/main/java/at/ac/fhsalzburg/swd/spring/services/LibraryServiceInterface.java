package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Media;

import java.util.Collection;
import java.util.List;

public interface LibraryServiceInterface {

	Library save(Library library);

	Library findById(Long id);

	List<Library> findByName(String name);

	void deleteById(Long id);

	Collection<Media> validateMedia(String mediaId);

	Collection<Edition> checkForAvailableEditions(Media media);

	Edition findAvailableEdition(Collection<Edition> copies);

	boolean isEditionAvailable(Edition edition);
	
	Iterable<String> getAllGenres();

	Iterable<String> getAllMediaTypes();
}
