package at.ac.fhsalzburg.swd.spring.startup;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import at.ac.fhsalzburg.swd.spring.model.Shelf;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;
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
	private GenreRepository genreRepository;

	@Autowired
	private MediaTypeRepository mediaTypeRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private MediaService mediaService;

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

		performCustomerCRUD();
		createLibraryWithMedia();
	}

	private void performCustomerCRUD() {
		// CREATE
		Customer customer = new Customer();
		customer.setName("John Doe");
		customer.setBirthDate(new Date());
		customer.setLoanLimit(5);
		customer.setCustomerType(Customer.CustomerType.REGULAR);
		customerService.save(customer);
		System.out.println("Customer created: " + customer);

		// READ
		Customer retrievedCustomer = customerService.findById(customer.getId());
		if (retrievedCustomer != null) {
			System.out.println("Customer retrieved: " + retrievedCustomer);
		} else {
			System.out.println("Customer not found");
		}

		// UPDATE
		if (retrievedCustomer != null) {
			retrievedCustomer.setName("Updated Name");
			retrievedCustomer.setLoanLimit(10);
			customerService.save(retrievedCustomer);
			System.out.println("Customer updated: " + retrievedCustomer);
		}

		// DELETE
		if (retrievedCustomer != null) {
			customerService.deleteById(retrievedCustomer.getId());
			System.out.println("Customer deleted with ID: " + retrievedCustomer.getId());
		}
	}

	private void createLibraryWithMedia() {
		// Create sections and shelves for the library
		Section sectionA = new Section("Section A");
		Section sectionB = new Section("Section B");
		Shelf shelfA1 = new Shelf(1, sectionA);
		Shelf shelfB1 = new Shelf(1, sectionB);

		// Assign shelves to sections
		sectionA.setShelves(List.of(shelfA1));
		sectionB.setShelves(List.of(shelfB1));

		// Create the library and save it with sections
		Library library = new Library(null, "Central Library", "123 Library St", Arrays.asList(sectionA, sectionB));
		libraryService.save(library);
		System.out.println("Library created: " + library);

		// Create a genre and media type for media items
		// Create and save the genre and media type
		Genre scienceFictionGenre = new Genre();
		scienceFictionGenre.setName("Science Fiction");
		scienceFictionGenre.setPrice(10.0);
		scienceFictionGenre = genreRepository.save(scienceFictionGenre);
		
		Genre FantasyGenre = new Genre();
		FantasyGenre.setName("Fantasy Genre");
		FantasyGenre.setPrice(10.0);
		FantasyGenre = genreRepository.save(FantasyGenre);
		
		
		;

		MediaType mediaType = new MediaType();
		mediaType.setTypeName("Book");
		// Save the MediaType entity before using it
		mediaType = mediaTypeRepository.save(mediaType);

		// Create media items and assign genre, type, and shelf
		Media book1 = new Media();
		book1.setName("The Time Machine");
		book1.setGenre(scienceFictionGenre);
		book1.setMediaType(mediaType);
		book1.setShelf(shelfA1);
		mediaService.save(book1);

		Media book2 = new Media();
		book2.setName("Dune");
		book2.setGenre(FantasyGenre);
		book2.setMediaType(mediaType);
		book2.setShelf(shelfB1);
		mediaService.save(book2);

		System.out.println("Media items created and added to library shelves.");
	}
}
