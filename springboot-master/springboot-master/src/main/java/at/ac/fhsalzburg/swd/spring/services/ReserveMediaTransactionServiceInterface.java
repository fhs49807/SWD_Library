package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public interface ReserveMediaTransactionServiceInterface {

    void reserveMediaForCustomer(Customer customer, Media media);

}
