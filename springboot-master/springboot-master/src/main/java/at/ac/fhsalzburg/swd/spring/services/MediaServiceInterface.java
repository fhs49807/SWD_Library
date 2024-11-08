package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;

public interface MediaServiceInterface {

	// add/ update genres
	public abstract void saveGenre(Genre genre);
	
	//add/ update media
	public abstract void save(Media media);
	
}
