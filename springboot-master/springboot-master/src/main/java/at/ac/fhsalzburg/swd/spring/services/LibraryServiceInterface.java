package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Media;

public interface LibraryServiceInterface {

	// add or update library information
	public abstract Library save(Library library);

	// retrieve library details by id
	public abstract Library findById(Long id);

	// search library by name
	public abstract List<Library> findByName(String name);

	// ??
	public abstract void deleteById(Long id);

	// check if media item exists
	public abstract Collection<Media> validateMedia(String mediaId);

	// check if media has available editions
	public abstract Collection<Edition> checkForAvailableEditions(Media media);

	// locate available edition
	public abstract Edition findAvailableEdition(Collection<Edition> copies);

	// verify if edition is available
	public abstract boolean isEditionAvailable(Edition edition);

	
}
