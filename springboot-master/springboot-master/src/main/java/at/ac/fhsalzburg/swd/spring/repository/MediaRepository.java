package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.Mediatype;

@Repository
public interface MediaRepository extends CrudRepository<Media, Integer> {

    Media findByName(String name);

    Iterable<Media> findByAvailabilityStatus(String availabilityStatus);
    Iterable<Media> findByMediaType(Mediatype mediatype);
}