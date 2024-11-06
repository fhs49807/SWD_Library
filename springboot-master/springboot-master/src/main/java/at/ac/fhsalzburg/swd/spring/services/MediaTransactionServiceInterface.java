package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.Shelf;

public interface MediaTransactionServiceInterface {

	public abstract MediaTransaction loanMedia(Date transactionDate, Date expirationDate, Date expectedReturnDate,
			Shelf shelf, Customer customer, Collection<Edition> editions);

	// reserveMedia applies to Medium and not edition
	public abstract MediaTransaction reserveMedia(Date transactionDate, Customer customer,
			Collection<Media> mediaItems);

	public abstract MediaTransaction returnMedia(Date transactionDate, Customer customer, Collection<Edition> editions);

}
