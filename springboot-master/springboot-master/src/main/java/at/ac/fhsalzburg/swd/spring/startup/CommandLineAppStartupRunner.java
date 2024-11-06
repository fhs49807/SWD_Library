package at.ac.fhsalzburg.swd.spring.startup;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.Book;
import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Library;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.MediaType;
import at.ac.fhsalzburg.swd.spring.model.Section;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.CustomerService;
import at.ac.fhsalzburg.swd.spring.services.LibraryService;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ProductServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	UserServiceInterface userService;

	@Autowired
	ProductServiceInterface productService;

	@Autowired
	OrderServiceInterface orderService;

//	-------------------------------------------------

	@Autowired
	private CustomerService customerService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaTransactionService mediaTransactionService;

	// Initialize System with preset accounts and stocks
	@Override
	@Transactional // this method runs within one database transaction; performing a commit at the
					// end
	public void run(String... args) throws Exception {

		if (userService.getByUsername("admin") != null)
			return; // data already exists -> return

		userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(), "admin", "ADMIN");

		productService.addProduct("first product", 3.30f);
		User user = userService.getAll().iterator().next();
		user.setCredit(100l);
		user = userService.getByUsername("admin");
		orderService.addOrder(new Date(), user, productService.getAll());

		// Create sample customer
		Customer customer = new Customer();
		customer.setName("John Doe");
		customer.setBirthDate(new Date());
		customer.setLoanLimit(5);
		customer.setCustomerType(Customer.CustomerType.REGULAR);
		customerService.save(customer);

		createMedia();
	}

	public void createMedia() {

		// Create sample library with sections
		Section sectionA = new Section("Section A");
		Section sectionB = new Section("Section B");
		Library library = new Library(null, "Central Library", "123 Library St", Arrays.asList(sectionA, sectionB));
		libraryService.save(library);

		// TODO: Create Media and Editions

		// TODO: Create MediaTransaction with multiple Editions

	}

}
