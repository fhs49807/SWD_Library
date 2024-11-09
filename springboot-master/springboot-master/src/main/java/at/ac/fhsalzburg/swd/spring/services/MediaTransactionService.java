package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;

@Service
public class MediaTransactionService implements MediaTransactionServiceInterface {

	@Autowired
	private MediaTransactionRepository mediaTransactionRepository;

	@Autowired
	private EditionRepository editionRepository;
	
	@Autowired
    private InvoiceServiceInterface invoiceService;
	
	// creates loan record for a customer
	// marks each loaned item as unavailable
	// saves transaction to repository
	@Override
	public MediaTransaction createLoanRecord(Customer customer, Date dueDate, Collection<Edition> editions) {
		MediaTransaction loan = new MediaTransaction(new Date(), dueDate, Collections.emptyList(), editions, customer);

		for (Edition edition : editions) {
			edition.setAvailable(false); // Mark each edition as unavailable
		}
		editionRepository.saveAll(editions); // Update availability of all editions
		return mediaTransactionRepository.save(loan);
	}

	// finds all loan transactions for specific customer
	@Override
	public Collection<MediaTransaction> findLoansByUser(Customer customer) {
		return mediaTransactionRepository.findByCustomer(customer);
	}

	// get all loans currently entered in the system
	@Override
	public Collection<MediaTransaction> getAllLoans() {
		return (Collection<MediaTransaction>) mediaTransactionRepository.findAll();
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
            invoiceService.deductAmount(transaction.getCustomer(), transaction);
        }

        // transaktion aktualisieren
        mediaTransactionRepository.save(transaction);
    }
}
