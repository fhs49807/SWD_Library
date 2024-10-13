package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.ReturnMedia;


@Repository
public interface ReturnMediaRepository extends CrudRepository<ReturnMedia, String>{

}
