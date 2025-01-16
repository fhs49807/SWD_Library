package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

import java.time.LocalDate;
import java.util.List;

public interface EditionServiceInterface {

	List<Edition> findAvailableEditions(Media media, LocalDate startDate, LocalDate endDate);

	void addEditionsForMedia(Media media, int count);

	List<Long> getEditionIdsForMedia(Long mediaId);

}
