package at.ac.fhsalzburg.swd.spring.startup;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.repository.CustomerRepository;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ProductServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        //		if (userService.getByUsername("admin") != null) return;
        //
        //		userService.addUser("admin", "Admin", "admin@gmail.com", "123", new Date(123), "123", "ADMIN");
        //
        //		productService.addProduct("firstArticle", 2.00f);
        //        User user = userService.getAll().iterator().next();	// User user = userService.getByUsername
        //        ("admin");
        //        user.setCredit(100l);
        //        user = userService.getByUsername("admin");
        //        orderService.addOrder(new Date(), user, productService.getAll());

        Customer customer = new Customer(null, "Student", 5, "Test Name");
        customerRepository.save(customer);

        //		createMedia();
    }

    public void createMedia() {
        //		mediaService.saveGenre(new Genre("Fantasy"));
        //        Genre fantasy = mediaService.searchGenreByName("Fantasy");
    }

    // create library (physical?)

    // create media --> genre, name, physical location, etc?

    // create customers

    // reserve media

    // loan media

    // return media

    // create invoice
}
