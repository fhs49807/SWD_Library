package at.ac.fhsalzburg.swd.spring.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.services.CustomerServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ProductServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;

import java.util.Date;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

	@Autowired
	UserServiceInterface userService;

	@Autowired
	ProductServiceInterface productService;

	@Autowired
	OrderServiceInterface orderService;

	@Autowired
	CustomerServiceInterface customerService;

	@Autowired
	private CustomerRepository customerRepository;// TODO: remove --> autowire this in customerService

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		if (userService.getByUsername("admin") != null)
			return;

		userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(), "admin", "ADMIN");

		productService.addProduct("first product", 3.30f);
		User user = userService.getByUsername("admin");
		user.setCredit(100L);
		orderService.addOrder(new Date(), user, productService.getAll());

		// -------------------------------------------------------------

		Customer customer = new Customer(1, null, "Student", 5, "Test Name");
		customerRepository.save(customer);

		System.out.println("Customer created: " + customer.getName() + " with ID: " + customer.getCustomerID());

    
	}
	
	public void createMedia() {
		//TODO
	}
	
//  create library (physical?)

//  create media --> genre, name, physical location, etc?

//  create customers

//  reserve media

//  loan media

//  return media

//  create invoice    
	
}
