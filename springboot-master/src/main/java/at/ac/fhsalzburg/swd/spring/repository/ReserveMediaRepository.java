package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.ReserveMedia;


@Repository
public interface ReserveMediaRepository extends CrudRepository<ReserveMedia, String>{

}