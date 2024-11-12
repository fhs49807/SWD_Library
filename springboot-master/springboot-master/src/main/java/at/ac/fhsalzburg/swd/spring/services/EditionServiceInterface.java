package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

import java.util.Collection;

public interface EditionServiceInterface {

    Collection<Edition> findByMediaAndAvailable(Media media);
}
