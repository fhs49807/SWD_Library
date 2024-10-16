package at.ac.fhsalzburg.swd.spring.startup;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ProductServiceInterface;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserServiceInterface userService;

    @Autowired
    ProductServiceInterface productService;

    @Autowired
    OrderServiceInterface orderService;
  

    // Initialize System with preset accounts and stocks
    @Override
    @Transactional // this method runs within one database transaction; performing a commit at the
                   // end
    public void run(String... args) throws Exception {
    	
    	if (userService.getByUsername("admin")!=null) return; // data already exists -> return
    	
    	userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(), "admin","ADMIN");
    	
        productService.addProduct("first product", 3.30f);
        User user = userService.getAll().iterator().next();
        user.setCredit(100l);
        user = userService.getByUsername("admin");
        orderService.addOrder(new Date(), user, productService.getAll());
        
        
//        create library (physical?)
        
//        create media --> genre, name, physical location, etc?
        
//        create customers
        
//        reserve media
        
//        loan media
        
//        return media
        
//        create invoice
        
    }
}
