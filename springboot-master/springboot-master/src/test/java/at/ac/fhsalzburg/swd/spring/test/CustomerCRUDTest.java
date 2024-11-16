package at.ac.fhsalzburg.swd.spring.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.UserService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Ensures that changes to the database are rolled back after each test
public class CustomerCRUDTest {

	@Autowired
	private UserService userService;

	@Test
	public void testUserCRUDOperations() {
		// CREATE
		boolean created = userService.addUser("john.doe", "John Doe", "john.doe@example.com", "123456789", new Date(),
				"securepassword", "Customer", User.CustomerType.REGULAR, 5);
		
		// READ
		assertTrue(created, "User should be created successfully");
		User retrievedUser = userService.getByUsername("john.doe");
		assertNotNull(retrievedUser, "User should be retrieved after creation");
		assertEquals("John Doe", retrievedUser.getFullname(), "User full name should match");
		assertEquals(5, retrievedUser.getLoanLimit(), "User loan limit should match");
		assertEquals(User.CustomerType.REGULAR, retrievedUser.getCustomerType(), "User customer type should match");
		System.out.println("User created: " + retrievedUser);

		// UPDATE
		retrievedUser.setFullname("Updated Name");
		retrievedUser.setLoanLimit(10);
		userService.addUser(retrievedUser); // Reuse addUser for updating
		User updatedUser = userService.getByUsername("john.doe");
		assertNotNull(updatedUser, "Updated user should not be null");
		assertEquals("Updated Name", updatedUser.getFullname(), "User full name should be updated");
		assertEquals(10, updatedUser.getLoanLimit(), "User loan limit should be updated");
		System.out.println("User updated: " + updatedUser);

		// DELETE
		userService.deleteUser(updatedUser.getUsername());
		User deletedUser = userService.getByUsername("john.doe");
		assertNull(deletedUser, "User should be null after deletion");
		System.out.println("User deleted with username: " + updatedUser.getUsername());
	}
}