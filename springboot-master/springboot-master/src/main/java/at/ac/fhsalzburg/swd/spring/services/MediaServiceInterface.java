package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;

public interface MediaServiceInterface {

	// add/ update genres
	public abstract void saveGenre(Genre genre);

	// add/ update media
	public abstract void save(Media media);

	Media findById(Long id);

	// get all currently existing genres
	Iterable<String> getAllGenres();

	// get all currently existing mediaTypes
	Iterable<String> getAllMediaTypes();


	Iterable<Media> searchMediaByGenreAndType(String genre, String type);

    Boolean addMedia(Media media);

	Media findByName(String name);

}
