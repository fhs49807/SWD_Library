package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;

@Service
public class CustomerService {
	@SuppressWarnings("unused")
	@Autowired
    private CustomerRepository customerRepository;

}