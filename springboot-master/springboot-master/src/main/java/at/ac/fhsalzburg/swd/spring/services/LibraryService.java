package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.LibraryRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;

@Service
public class LibraryService implements LibraryServiceInterface {

	@Autowired
	private LibraryRepository libraryRepository;

	@Autowired
	private MediaRepository mediaRepository; //besser in MediaService

    //add MediaService @Autowired

	@Autowired
	private EditionRepository editionRepository;

	@Autowired
	private MediaTransactionRepository mediaTransactionRepository;

	// save library to repository
	@Override
	public Library save(Library library) {
		return libraryRepository.save(library);
	}

	// find library by id
	@Override
	public Library findById(Long id) {
		return libraryRepository.findById(id).orElse(null);
	}

	//find library by name
	@Override
	public List<Library> findByName(String name) {
		return libraryRepository.findByName(name);
	}

	// delete library by id
	@Override
	public void deleteById(Long id) {
		libraryRepository.deleteById(id);

	}

	// checks if media with specific id exists
	@Override
	public Collection<Media> validateMedia(String mediaId) {
		return mediaRepository.findById(Long.parseLong(mediaId)).map(List::of).orElseGet(List::of);
	}

	// checks if editions for specific media are available
	@Override
	public Collection<Edition> checkForAvailableEditions(Media media) {
		return editionRepository.findByMediaAndAvailable(media);

	}

	// finds first available edition from collection of editions
	@Override
	public Edition findAvailableEdition(Collection<Edition> editions) {
		for (Edition edition : editions) {
			if (isEditionAvailable(edition)) {
				return edition;
			}
		}
		return null;
	}

	// checks if a specific edition is available
	@Override
	public boolean isEditionAvailable(Edition edition) {
		return editionRepository.existsByIdAndAvailable(edition.getId(), true);
	}

	

}
