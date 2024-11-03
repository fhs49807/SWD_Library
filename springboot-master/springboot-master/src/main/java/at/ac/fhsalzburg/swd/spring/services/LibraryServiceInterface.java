package at.ac.fhsalzburg.swd.spring.services;

import java.util.Map;

public interface LibraryServiceInterface {

    void createLibrary(String name, String location, Map<String, Integer> sectionNumberOfShelvesMap);

}
