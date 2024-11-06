package at.ac.fhsalzburg.swd.spring.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.repository.LibraryRepository;

@Service
public class LibraryService implements LibraryServiceInterface{

	@Autowired
    private LibraryRepository libraryRepository;

    @Override
    public Library save(Library library) {
        return libraryRepository.save(library);
    }

    @Override
    public Library findById(Long id) {
        return libraryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Library> findByName(String name) {
        return libraryRepository.findByName(name);
    }

    @Override
    public List<Library> findByLocation(String location) {
        return libraryRepository.findByLocation(location);
    }

    @Override
    public void deleteById(Long id) {
        libraryRepository.deleteById(id);	
	}

}
