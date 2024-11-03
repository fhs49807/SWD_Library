package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LibraryService implements LibraryServiceInterface {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private SectionServiceInterface sectionService;

    @Override
    public void createLibrary(String name, String location, Map<String, Integer> sectionNumberOfShelvesMap) {
        Library library = new Library(name, location);
        libraryRepository.save(library);

        sectionService.createSection(library, sectionNumberOfShelvesMap);
    }
}
