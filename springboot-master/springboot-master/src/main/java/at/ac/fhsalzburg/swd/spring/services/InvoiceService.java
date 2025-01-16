package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Invoice;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class InvoiceService implements InvoiceServiceInterface {

	private final InvoiceRepository invoiceRepository;

	// Constructor injection
	public InvoiceService(InvoiceRepository invoiceRepository) {
		this.invoiceRepository = invoiceRepository;
	}

	// erstellt & speichert die Rg nach abzug der fälligen beträge -> userservice
	// updateUser()
	@Override
	public void deductAmount(User user, MediaTransaction transaction) {
		double penaltyAmount = calculatePenalty(transaction);
		if (penaltyAmount > 0) {
			// liste man. erstelln
			List<MediaTransaction> transactions = Arrays.asList(transaction);

			Invoice invoice = new Invoice(new Date(), false, transactions, user);
			invoice.setTotalAmount(penaltyAmount);
			invoiceRepository.save(invoice);
		}
	}

	// berechnet mahngebühr basierend auf überfälligkeit -> deductAmount()
	public double calculatePenalty(MediaTransaction transaction) {
		// mahngebühren calc: 1€ pro tag verspätung
		long overdueDays = DAYS.between(transaction.getReturnDate(), transaction.getLast_possible_return_date());
		return overdueDays > 0 ? overdueDays * 1.0 : 0.0;
	}

	@Override
	public double getOutstandingBalance(User user) {
		Double outstandingBalance = invoiceRepository.calculateOutstandingBalance(user);
		return outstandingBalance != null ? outstandingBalance : 0.0;
	}

}
