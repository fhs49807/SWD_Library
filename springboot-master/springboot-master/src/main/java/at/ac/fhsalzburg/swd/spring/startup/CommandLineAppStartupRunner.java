package at.ac.fhsalzburg.swd.spring.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ProductServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;

import java.sql.Date;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	OrderServiceInterface orderService;

	@Autowired
	ProductServiceInterface productService;

	@Autowired
	UserServiceInterface userService;

	///@Autowired
	///MediaServiceInterface mediaService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private CustomerRepository customerRepository;// TODO: remove --> autowire this in customerService

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		if (userService.getByUsername("admin") != null) return;
		
		userService.addUser("admin", "Admin", "admin@gmail.com", "123", new Date(123), "123", "ADMIN");
		
		productService.addProduct("firstArticle", 2.00f);
        User user = userService.getAll().iterator().next();	// User user = userService.getByUsername("admin");
        user.setCredit(5l);
        user = userService.getByUsername("admin");
        orderService.addOrder(new Date(123), user, productService.getAll());
		
		// Customer customer = new Customer(1, null, "Student", 5, "Test Name");
		// customerRepository.save(customer);

		// System.out.println("Customer created: " + customer.getName() + " with ID: " +
		// customer.getCustomerID());

		createMedia();
		mediaService.saveGenre(new Genre());
		searchGenreByName(String name);
    }

	public void createMedia() {
		mediaService.saveGenre(new Genre("Action"));
        Genre fantasy = mediaService.searchGenreByName("Action");
	}

	// create library (physical?)

	// create media --> genre, name, physical location, etc?

	// create customers

	// reserve media

	// loan media

	// return media

	// create invoice
}