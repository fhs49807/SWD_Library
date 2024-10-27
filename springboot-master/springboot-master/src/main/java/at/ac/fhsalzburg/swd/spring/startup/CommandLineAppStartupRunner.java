package at.ac.fhsalzburg.swd.spring.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.services.CustomerServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;

import java.sql.Date;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	OrderServiceInterface orderService;

	@Autowired
	CustomerServiceInterface customerService;

	@Autowired
	private CustomerRepository customerRepository;// TODO: remove --> autowire this in customerService

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		// if (userService.getByUsername("admin") != null)
		// return;
		//
		// userService.addUser("admin", "Administrator", "admin@work.org", "123", new
		// Date(), "admin", "ADMIN");
		//
		// productService.addProduct("first product", 3.30f);
		// User user = userService.getByUsername("admin");
		// user.setCredit(100L);
		// orderService.addOrder(new Date(), user, productService.getAll());

		// -------------------------------------------------------------

		//Customer customer = new Customer(1, null, "Student", 5, "Test Name");
		//customerRepository.save(customer);

		//System.out.println("Customer created: " + customer.getName() + " with ID: " + customer.getCustomerID());

		// Create: Erstelle ein Customer-Objekt und speichere es
		Customer newCustomer = new Customer(1, Date.valueOf("1990-01-01"), "Standard", 5, "John Doe");
		customerRepository.save(newCustomer);
		System.out.println("Customer created: " + newCustomer);

		// Read: Lade einen Customer anhand seiner ID
		Customer customer = customerRepository.findById(1).orElse(null);
		System.out.println("Customer read: " + customer);

		// Update: Ändere die Attribute des geladenen Customer und speichere das
		// aktualisierte Objekt
		if (customer != null) {
			customer.setLoanLimit(10);
			customerRepository.save(customer);
			System.out.println("Customer updated: " + customer);
		}

		// Delete: Lösche den Customer anhand seiner ID
		customerRepository.deleteById(1);
		System.out.println("Customer deleted with ID 1");

	}

	public void createMedia() {
		// TODO
	}

	// create library (physical?)

	// create media --> genre, name, physical location, etc?

	// create customers

	// reserve media

	// loan media

	// return media

	// create invoice
}