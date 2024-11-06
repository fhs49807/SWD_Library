package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Ensures that changes to the database are rolled back after each test
public class CustomerCRUDTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testCustomerCRUDOperations() {
        // CREATE
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setBirthDate(new Date());
        customer.setLoanLimit(5);
        customer.setCustomerType(Customer.CustomerType.REGULAR);
        customerService.save(customer);

        assertNotNull(customer.getId(), "Customer ID should not be null after save");
        System.out.println("Customer created: " + customer);

        // READ
        Customer retrievedCustomer = customerService.findById(customer.getId());
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");
        assertEquals("John Doe", retrievedCustomer.getName(), "Customer name should match");
        assertEquals(5, retrievedCustomer.getLoanLimit(), "Customer loan limit should match");
        System.out.println("Customer retrieved: " + retrievedCustomer);

        // UPDATE
        retrievedCustomer.setName("Updated Name");
        retrievedCustomer.setLoanLimit(10);
        customerService.save(retrievedCustomer);

        Customer updatedCustomer = customerService.findById(retrievedCustomer.getId());
        assertNotNull(updatedCustomer, "Updated customer should not be null");
        assertEquals("Updated Name", updatedCustomer.getName(), "Customer name should be updated");
        assertEquals(10, updatedCustomer.getLoanLimit(), "Customer loan limit should be updated");
        System.out.println("Customer updated: " + updatedCustomer);

        // DELETE
        customerService.deleteById(updatedCustomer.getId());
        Customer deletedCustomer = customerService.findById(updatedCustomer.getId());
        assertNull(deletedCustomer, "Customer should be null after deletion");
        System.out.println("Customer deleted with ID: " + updatedCustomer.getId());
    }
}
