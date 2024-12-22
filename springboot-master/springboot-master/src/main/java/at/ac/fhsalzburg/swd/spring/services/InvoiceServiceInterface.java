package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;

public interface InvoiceServiceInterface {

	// Deduct outstanding amount from user's account
	void deductAmount(User user, MediaTransaction transaction);

	// Get the outstanding balance for a user
	double getOutstandingBalance(User user);

	// Calculate the penalty for a media transaction
	double calculatePenalty(MediaTransaction transaction);
}
