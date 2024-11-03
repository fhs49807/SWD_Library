package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Library;

import java.util.Map;

public interface SectionServiceInterface {

	void createSection(Library library, Map<String, Integer> sectionNumberOfShelvesMap);

}
