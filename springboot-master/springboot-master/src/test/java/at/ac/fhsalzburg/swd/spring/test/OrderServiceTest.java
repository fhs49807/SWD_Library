package at.ac.fhsalzburg.swd.spring.test;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.BDDMockito.*;

import at.ac.fhsalzburg.swd.spring.model.Order;
import at.ac.fhsalzburg.swd.spring.model.Product;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.OrderRepository;
import at.ac.fhsalzburg.swd.spring.repository.ProductRepository1;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;
import at.ac.fhsalzburg.swd.spring.services.OrderService;
import at.ac.fhsalzburg.swd.spring.services.OrderServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;

import java.util.ArrayList;
import java.util.Date;

// good explanation of how to test services: https://www.arhohuttunen.com/spring-boot-unit-testing/
// explanation of stubbing: https://www.baeldung.com/mockito-unnecessary-stubbing-exception

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@ActiveProfiles("test")
public class OrderServiceTest {
	
	
	private OrderRepository orderRepo;
    private OrderServiceInterface orderService;    
    private UserServiceInterface userService;
	
	@BeforeEach
    void setupService() {
		// we only want to test the order logic, so we mock everything else (repositories and services)
		userService = mock(UserService.class);
		orderRepo = mock(OrderRepository.class);
        orderService = new OrderService(userService, orderRepo);
    }
       
        

    @Test
    public void whenNewOrderWithCredit_thenReturnOrder() {
        // given    	
    	Date now = new Date();
    	User customer = new User("test","junittest","test@test.tst", "123456", now, "none", "USER", null);    	
    	Product product = new Product("product 1",120);
    	ArrayList<Product> products = new ArrayList<Product>();
    	products.add(product);    	
    	Order order = new Order(now, customer, products);
    	
    	//user service is mocked, check for credit will always return true
    	given(userService.hasCredit(any(User.class))).willReturn(true);
    	//repo is mocked but should return the order in case of success    	
        given(orderRepo.save(any(Order.class))).willReturn(order);
    	
        // when
    	Order orderActual = orderService.addOrder(now, customer, products);

        // then
    	assertEquals(order, orderActual);
    	assertArrayEquals( products.toArray(), orderActual.getProducts().toArray());
        assertEquals(customer, orderActual.getCustomer());
    }
    
    @Test
    public void whenNewOrderNoCredit_thenReturnNull() {
    	// given
    	Date now = new Date();
    	User customer = new User("test","junittest","test@test.tst", "123456", now, "none", "USER", null);
    	customer.setCredit(119l);
    	Product product = new Product("product 1",120);
    	ArrayList<Product> products = new ArrayList<Product>();
    	products.add(product);    	
    	Order order = new Order(now, customer, products);    	
    	
    	//user service is mocked, check for credit will always return false
    	given(userService.hasCredit(any(User.class))).willReturn(false);
    	//repo is mocked but should return the order in case of success
    	//lenient is neccessary here, because otherwise mockito would return an error due to stubbing (mocking something that is not called)
    	//	if the customer has no credit, the order is not saved, so the save method is not called
    	//	but if our service logic fails, the order would be created and so the save method would be called (but would return null because it is mocked) 
        lenient().when(orderRepo.save(any(Order.class))).thenReturn(order);
    	
        // when
    	Order orderActual = orderService.addOrder(now, customer, products);

        // then
    	assertNull(orderActual);   	
    }

}
