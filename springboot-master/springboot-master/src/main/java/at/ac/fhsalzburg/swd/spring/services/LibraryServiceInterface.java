package at.ac.fhsalzburg.swd.spring.services;

import java.util.List;

import at.ac.fhsalzburg.swd.spring.model.Library;

public interface LibraryServiceInterface {

	public abstract Library save(Library library);

	public abstract Library findById(Long id);

	public abstract List<Library> findByName(String name);

	public abstract List<Library> findByLocation(String location);

	public abstract void deleteById(Long id);

}
