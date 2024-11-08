package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Media;

@Service
public class InvoiceService implements InvoiceServiceInterface{

	@Override
	public void deductAmount(Customer customer, Media media) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getOutstandingBalance(Customer customer) {
		// TODO Auto-generated method stub
		return 0;
	}

}
