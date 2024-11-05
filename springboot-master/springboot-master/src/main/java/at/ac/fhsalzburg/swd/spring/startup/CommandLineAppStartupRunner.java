package at.ac.fhsalzburg.swd.spring.startup;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    OrderServiceInterface orderService;

    @Autowired
    ProductServiceInterface productService;

    @Autowired
    MediaServiceInterface mediaService;

    @Autowired
    UserServiceInterface userService;

    @Autowired
    private CustomerRepository customerRepository;// TODO: remove --> autowire this in customerService

    @Autowired
    private LibraryServiceInterface libraryService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {


        if (userService.getByUsername("admin") != null)
            return; // data already exists -> return

        userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(123), "admin", "ADMIN");

        productService.addProduct("first product", 3.30f); // was ist ein Product?
        User user = userService.getAll().iterator().next();
        user.setCredit(100l);
        user = userService.getByUsername("admin");
        orderService.addOrder(new Date(), user, productService.getAll());


        Customer customer = new Customer(null, "Student", 5, "Test Name");
        customerRepository.save(customer);

        // Call the CRUD operations for Customer
        performCustomerCRUD();

        createLibrary();

        //		createMedia();
    }

    private void performCustomerCRUD() {
        // CREATE
        Customer customer = new Customer(new Date(), "Student", 5, "Test Name");
        customer = customerRepository.save(customer); // Save and update reference with generated ID
        System.out.println("Customer created: " + customer);

        // READ
        customerRepository.findById(String.valueOf(customer.getCustomerId()))
            .ifPresentOrElse(
                retrievedCustomer -> System.out.println("Customer retrieved: " + retrievedCustomer),
                () -> System.out.println("Customer not found")
                            );

        // UPDATE
        customerRepository.findById(String.valueOf(customer.getCustomerId())).ifPresent(retrievedCustomer -> {
            retrievedCustomer.setName("Updated Name");
            retrievedCustomer.setLoanLimit(10);
            customerRepository.save(retrievedCustomer);
            System.out.println("Customer updated: " + retrievedCustomer);
        });

        // DELETE
        customerRepository.findById(String.valueOf(customer.getCustomerId())).ifPresent(retrievedCustomer -> {
            customerRepository.delete(retrievedCustomer);
            System.out.println("Customer deleted with ID: " + retrievedCustomer.getCustomerId());
        });
    }

    private void createLibrary() {
        Map<String, Integer> sectionNumberOfShelvesMap = new HashMap<>();
        sectionNumberOfShelvesMap.put("IT", 1);
        sectionNumberOfShelvesMap.put("Fantasy", 4);

        libraryService.createLibrary("University Library", "Salzburg", sectionNumberOfShelvesMap);
    }

    public void createMedia() {
        //		mediaService.saveGenre(new Genre("Fantasy"));
        //        Genre fantasy = mediaService.searchGenreByName("Fantasy");
    }

    // create media --> genre, name, physical location, etc?

    // create customers

    // reserve media

    // loan media

    // return media

    // create invoice


}
