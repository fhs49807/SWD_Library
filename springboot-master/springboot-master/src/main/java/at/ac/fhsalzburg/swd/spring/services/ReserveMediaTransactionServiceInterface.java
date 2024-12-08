package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import javassist.NotFoundException;

import java.util.Collection;
import java.util.Date;

public interface ReserveMediaTransactionServiceInterface {

    void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate,
        Date reserveEndDate) throws NotFoundException;

    ReserveMediaTransaction getLatestReservation(Long mediaId, String username);

    Collection<ReserveMediaTransaction> findReservationsForUser(User username);
}
