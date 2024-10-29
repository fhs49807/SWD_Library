package at.ac.fhsalzburg.swd.spring.repository;

import at.ac.fhsalzburg.swd.spring.model.ReturnMedia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReturnMediaRepository extends CrudRepository<ReturnMedia, Long> {

    // List<ReturnMedia> findByCustomer(Customer customer); // das gehört hier nicht hin

    List<ReturnMedia> findByReturnDate(Date returnDate);

}
