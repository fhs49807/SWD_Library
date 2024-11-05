package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.InvoiceRepository;

@Service
public class InvoiceService {
	@SuppressWarnings("unused")
	@Autowired
    private InvoiceRepository invoiceRepository;
}