package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import javassist.NotFoundException;

import java.util.Date;

public interface ReserveMediaTransactionServiceInterface {

    void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate,
        Date reserveEndDate) throws NotFoundException;

    ReserveMediaTransaction getLatestReservation(Long mediaId, String username);

}
