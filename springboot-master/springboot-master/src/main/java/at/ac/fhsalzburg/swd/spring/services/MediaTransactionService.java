package at.ac.fhsalzburg.swd.spring.services;

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
	public MediaTransaction createLoanRecord(User user, Date dueDate, Collection<Edition> editions) {
		MediaTransaction loan = new MediaTransaction(new Date(), dueDate, Collections.emptyList(), editions, user);

		for (Edition edition : editions) {
			edition.setAvailable(false); // Mark each edition as unavailable
		}
		editionRepository.saveAll(editions); // Update availability of all editions
		return mediaTransactionRepository.save(loan);
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
		
		// find customer by ID --> by username
		User user = userRepository.findByUsername(username);
	    if (user == null) {
	        throw new IllegalArgumentException("User not found");
	    }

		// find all editions by ID
		Collection<Edition> editions = StreamSupport
				.stream(editionRepository.findAllById(editionIds).spliterator(), false).collect(Collectors.toList());

		// check if editions are available for loan
		for (Edition edition : editions) {
			if (!edition.isAvailable()) {
				throw new IllegalStateException("Edition with ID " + edition.getId() + " is not available for loan!");
			}
		}

		// create new MediaTransaction
		MediaTransaction transaction = new MediaTransaction();
		transaction.setUser(user);
		transaction.setEditions(editions);
		transaction.setTransactionDate(new Date());
		transaction.setExpirationDate(dueDate);
		transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

		// mark edition as unavailable
		for (Edition edition : editions) {
			edition.setAvailable(false);
			editionRepository.save(edition); // update edition availability in repository
		}

		// save transaction in repository
		return mediaTransactionRepository.save(transaction);
	}

	@Override
	public void returnMedia(Long transactionId) {
		MediaTransaction transaction = mediaTransactionRepository.findById(transactionId)
				.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		// rückgabe date aktualisieren
		transaction.setReturnDate(new Date());
		transaction.setStatus(MediaTransaction.TransactionStatus.COMPLETED);

		// editionen als verfügbar markieren
		for (Edition edition : transaction.getEditions()) {
			edition.setAvailable(true);
			editionRepository.save(edition);
		}

		// gebühren calc & evt Rg erstellen
		if (transaction.getReturnDate().after(transaction.getExpirationDate())) {
			invoiceService.deductAmount(transaction.getUser(), transaction);
		}

		// transaktion aktualisieren
		mediaTransactionRepository.save(transaction);
	}

}
