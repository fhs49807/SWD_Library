package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;

@Service
public class CustomerService implements CustomerServiceInterface {

	@Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Collection<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

}