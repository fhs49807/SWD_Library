package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EditionService implements EditionServiceInterface {

    @Autowired
    private EditionRepository editionRepository;

    public List<Edition> findByMediaAndAvailable(Media media) {
        return editionRepository.findByMediaAndAvailable(media);
    }
}
