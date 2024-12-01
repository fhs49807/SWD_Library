package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.User;

import java.util.Date;

public interface ReserveMediaTransactionServiceInterface {

    void reserveMediaForCustomer(String userName, Long mediaId, Date reserveStartDate, Date reserveEndDate);

}
