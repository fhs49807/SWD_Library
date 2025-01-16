package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
	public void addEditionsForMedia(Media media, int count) {
		for (int i = 0; i < count; i++) {
			Edition edition = new Edition(media);
			editionRepository.save(edition);
		}
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
