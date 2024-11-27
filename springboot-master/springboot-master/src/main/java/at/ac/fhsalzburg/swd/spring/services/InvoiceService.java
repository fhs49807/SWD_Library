package at.ac.fhsalzburg.swd.spring.services;

import java.util.List;
import java.util.Arrays;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Invoice;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.InvoiceRepository;

@Service
public class InvoiceService implements InvoiceServiceInterface{
	
	@Autowired
    private InvoiceRepository invoiceRepository;


	//erstellt & speichert die Rg nach abzug der fälligen beträge
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

	//berechnet mahngebühr basierend auf überfälligkeit
	public double calculatePenalty(MediaTransaction transaction) {
        // mahngebühren calc: 1€ pro tag verspätung
        long overdueDays = (transaction.getReturnDate().getTime() - transaction.getExpirationDate().getTime()) / (1000 * 60 * 60 * 24);
        return overdueDays > 0 ? overdueDays * 1.0 : 0.0;
    }

    @Override
    public double getOutstandingBalance(User user) {
        Double outstandingBalance = invoiceRepository.calculateOutstandingBalance(user);
        return outstandingBalance != null ? outstandingBalance : 0.0;
    }

}
