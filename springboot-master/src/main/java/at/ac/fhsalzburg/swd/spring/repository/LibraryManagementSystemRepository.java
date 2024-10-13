package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.LibraryManagementSystem;

@Repository
public interface LibraryManagementSystemRepository extends CrudRepository<LibraryManagementSystem, String>{

}
