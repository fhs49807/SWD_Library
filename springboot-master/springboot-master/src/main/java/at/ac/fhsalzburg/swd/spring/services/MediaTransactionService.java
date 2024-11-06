package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.Shelf;

@Service
public class MediaTransactionService implements MediaTransactionServiceInterface{

	@Override
	public MediaTransaction loanMedia(Date transactionDate, Date expirationDate, Date expectedReturnDate, Shelf shelf,
			Customer customer, Collection<Edition> editions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaTransaction reserveMedia(Date transactionDate, Customer customer, Collection<Media> mediaItems) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaTransaction returnMedia(Date transactionDate, Customer customer, Collection<Edition> editions) {
		// TODO Auto-generated method stub
		return null;
	}

}
