package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.User;

import java.util.Date;

public interface ReserveMediaTransactionServiceInterface {

    void reserveMediaForCustomer(User user, String mediaName, Date reserveStartDate, Date reserveEndDate);

    void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate, Date reserveEndDate);

}
