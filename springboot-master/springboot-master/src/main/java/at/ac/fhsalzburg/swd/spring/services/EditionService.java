package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class EditionService implements EditionServiceInterface {

    @Autowired
    private EditionRepository editionRepository;

    @Override
    public List<Edition> findByMediaAndAvailable(Media media) {
        return editionRepository.findByMediaAndAvailable(media);
    }

    @Override
    public List<Edition> findAvailableForReserve(Media media, Date startDate, Date endDate) {
        return editionRepository.findAvailableForReserve(media, startDate, endDate);
    }
}
