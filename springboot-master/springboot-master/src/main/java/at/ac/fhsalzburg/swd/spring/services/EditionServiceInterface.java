package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EditionServiceInterface {

	Collection<Edition> findByMediaAndAvailable(Media media);

	List<Edition> findAvailableForReserve(Media media, Date startDate, Date endDate);

	Edition findFirstAvailableEdition(Collection<Edition> editions);

	boolean isEditionAvailable(Edition edition);

	void addEditionsForMedia(Media media, int count);

	List<Long> getEditionIdsForMedia(Long mediaId);

	void markEditionAsAvailable(Edition edition);

	void markEditionAsUnavailable(Edition edition, Date dueDate);

}
