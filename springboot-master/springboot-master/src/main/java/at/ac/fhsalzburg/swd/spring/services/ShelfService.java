package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Section;
import at.ac.fhsalzburg.swd.spring.model.Shelf;
import at.ac.fhsalzburg.swd.spring.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShelfService implements ShelfServiceInterface {

    @Autowired
    private ShelfRepository shelfRepository;

    @Override
    public void createShelves(Section section, int numnberOfShelves) {
        for (int i = 1; i <= numnberOfShelves; i++) {
            Shelf shelf = new Shelf(section, i);

            shelfRepository.save(shelf);
        }
    }
}
