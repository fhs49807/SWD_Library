package at.ac.fhsalzburg.swd.spring.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;

@Service
public class MediaTransactionService implements MediaTransactionServiceInterface {

	@Autowired
	private MediaTransactionRepository mediaTransactionRepository;

	@Autowired
	private EditionRepository editionRepository;

	@Autowired
	private InvoiceServiceInterface invoiceService;

	@Autowired
	private UserRepository userRepository;

	// creates loan record for a customer
	// marks each loaned item as unavailable
	// saves transaction to repository
	@Override
	public Collection<MediaTransaction> createLoanRecord(User user, Date dueDate, Collection<Edition> editions) {
	    Collection<MediaTransaction> transactions = editions.stream().map(edition -> {
	        MediaTransaction loan = new MediaTransaction(new Date(), dueDate, Collections.emptyList(), edition, user);
	        edition.setAvailable(false); // Mark the edition as unavailable
	        editionRepository.save(edition); // Persist availability change
	        return mediaTransactionRepository.save(loan);
	    }).collect(Collectors.toList());
	    return transactions;
	}

	// finds all loan transactions for specific customer
	@Override
	public Collection<MediaTransaction> findLoansByUser(User user) {
		return mediaTransactionRepository.findByUser(user);
	}

	// get all loans currently entered in the system
	@Override
	public Collection<MediaTransaction> getAllLoans() {
		return (Collection<MediaTransaction>) mediaTransactionRepository.findAll();
	}

	@Override
	public MediaTransaction loanMedia(String username, Collection<Long> editionIds, Date dueDate) {
	    // Validate dueDate
	    Date today = new Date();
	    if (dueDate.before(today)) {
	        throw new IllegalArgumentException("Loan date cannot be in the past.");
	    }

	    // Find user by username
	    User user = userRepository.findByUsername(username);
	    if (user == null) {
	        throw new IllegalArgumentException("User not found");
	    }

	    // Find all editions by their IDs
	    Collection<Edition> editions = StreamSupport
	            .stream(editionRepository.findAllById(editionIds).spliterator(), false)
	            .collect(Collectors.toList());

	    if (editions.isEmpty()) {
	        throw new IllegalArgumentException("No editions found for the provided IDs.");
	    }

	    // Process each edition separately
	    MediaTransaction transaction = null;
	    for (Edition edition : editions) {
	        if (!edition.isAvailable()) {
	            throw new IllegalStateException("Edition with ID " + edition.getId() + " is not available for loan!");
	        }

	        // Check if the user already has this edition on loan
	        boolean isAlreadyLoaned = mediaTransactionRepository.existsByUserAndEditionAndStatus(
	                user, edition, MediaTransaction.TransactionStatus.ACTIVE);
	        if (isAlreadyLoaned) {
	            throw new IllegalStateException("User already has this media on loan (Edition ID: " + edition.getId() + ").");
	        }

	        // Calculate transaction and expected return dates
	        Date transactionDate = new Date();
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(transactionDate);
	        int loanPeriodDays = 14;
	        calendar.add(Calendar.DAY_OF_YEAR, loanPeriodDays);
	        Date expectedReturnDate = calendar.getTime();

	        // Create and save the MediaTransaction
	        transaction = new MediaTransaction(transactionDate, dueDate, edition, user);
	        transaction.setExpectedReturnDate(expectedReturnDate);
	        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);
	        mediaTransactionRepository.save(transaction);

	        // Update edition's availability
	        edition.setAvailable(false);
	        edition.setDueDate(expectedReturnDate);
	        editionRepository.save(edition);
	    }

	    return transaction;
	}




	@Override
	public void returnMedia(Long transactionId) {
	    MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
	            .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

	    // Update the return date and status
	    transaction.setReturnDate(new Date());
	    transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

	    // Mark the edition as available
	    Edition edition = transaction.getEdition();
	    edition.setAvailable(true);
	    editionRepository.save(edition);

	    // Calculate fees and generate invoice if return is overdue
	    if (transaction.getReturnDate().after(transaction.getExpirationDate())) {
	        invoiceService.deductAmount(transaction.getUser(), transaction);
	    }

	    // Save the updated transaction
	    mediaTransactionRepository.save(transaction);
	}
	
	


}
