package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Section;
import at.ac.fhsalzburg.swd.spring.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SectionService implements SectionServiceInterface {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ShelfServiceInterface shelfService;

    @Override
    public void createSection(Library library, Map<String, Integer> sectionNumberOfShelvesMap) {
        for (String sectionName : sectionNumberOfShelvesMap.keySet()) {
            Section section = new Section(sectionName, library);
            sectionRepository.save(section);

            shelfService.createShelves(section, sectionNumberOfShelvesMap.get(sectionName));
        }
    }
}
