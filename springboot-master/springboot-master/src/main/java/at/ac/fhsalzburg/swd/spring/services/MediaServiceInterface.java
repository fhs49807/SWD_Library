package at.ac.fhsalzburg.swd.spring.services;

import java.util.Date;
import java.util.List;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.User;

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

	//search by media and genre + FSK
	Iterable<Media> searchMediaByGenreAndType(String genre, String type, User user);

    Boolean addMedia(Media media);

	Media findByName(String name);

	List<Long> getEditionIdsByMediaId(Long mediaId);

}
