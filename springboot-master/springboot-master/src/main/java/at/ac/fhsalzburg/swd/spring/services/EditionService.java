package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EditionService implements EditionServiceInterface {

	private final EditionRepository editionRepository;

	// Constructor Injection
	public EditionService(EditionRepository editionRepository) {
		this.editionRepository = editionRepository;
	}

	@Override
	public List<Long> getEditionIdsForMedia(Long mediaId) {
		return editionRepository.findEditionIdsByMediaId(mediaId);
	}

	@Override
	public void markEditionAsAvailable(Edition edition) {
		// TODO delete this, not used
		edition.setAvailable(true);
		edition.setDueDate(null); // Clear due date when making available
		editionRepository.save(edition);
	}

	@Override
	public void markEditionAsUnavailable(Edition edition, LocalDate dueDate) {
		edition.setAvailable(false);
		edition.setDueDate(dueDate);
		editionRepository.save(edition);
	}

	@Override
	public boolean isEditionAvailable(Edition edition) {
		return editionRepository.existsByIdAndAvailable(edition.getId(), true);
	}

	@Override
	public Edition findFirstAvailableEdition(Collection<Edition> editions) {
		for (Edition edition : editions) {
			if (isEditionAvailable(edition)) {
				return edition;
			}
		}
		return null;
	}

	@Override
	public void addEditionsForMedia(Media media, int count) {
		for (int i = 0; i < count; i++) {
			Edition edition = new Edition(media);
			edition.setMediaName(media.getName());
			editionRepository.save(edition);
		}
	}

	@Override
	public List<Edition> findByMediaAndAvailable(Media media) {
		// TODO delete this
		return editionRepository.findByMediaAndAvailable(media);
	}

	@Override
	public List<Edition> findAvailableEditions(Media media, LocalDate startDate, LocalDate endDate) {
		List<Edition> availableEditions = new ArrayList<>();
		List<Edition> loanedEditions = editionRepository.findLoanedEditions(media, startDate);
		List<Edition> reservedEditions = editionRepository.findReservedEditions(media, startDate, endDate);

		for (Edition edition : editionRepository.findByMedia(media)) {
			if (loanedEditions.contains(edition) || reservedEditions.contains(edition)) {
				continue;
			}

			availableEditions.add(edition);
		}

		return availableEditions;
	}
}
