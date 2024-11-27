package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;

import java.util.List;

public interface EditionServiceInterface {

    List<Edition> findByMediaAndAvailable(Media media);
}
