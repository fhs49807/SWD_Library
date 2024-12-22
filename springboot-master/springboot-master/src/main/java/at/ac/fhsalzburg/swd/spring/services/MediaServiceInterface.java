package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.User;

import java.util.Collection;
import java.util.List;

public interface MediaServiceInterface {

	Boolean addMedia(Media media);

	void save(Media media);

	Media findById(Long id);

	Collection<Media> findMediaById(long id);

	Iterable<Media> searchMediaByGenreAndType(String genre, String type, User user);

	Media findByName(String name);

	List<Long> getEditionIdsByMediaId(Long mediaId);

	

}
