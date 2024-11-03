package at.ac.fhsalzburg.swd.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Section;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {

	/* die übergebene id muss von Typ long sein.
	Aber grundsätzlich ist diese Methode nicht nötig, da sie bereits von der CrudRepository implementiert wird
	@Transactional(timeout = 10)
	Section findById(int id);*/

}
