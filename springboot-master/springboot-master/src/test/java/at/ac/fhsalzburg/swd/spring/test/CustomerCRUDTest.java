package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.enums.CustomerType;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
		boolean created = userService.addUser("johndoe", "John Doe", "john.doe@example.com", "123456789", new Date(),
				"securepassword", "USER", CustomerType.REGULAR, 5);
		assertTrue(created, "User should be created successfully");

		// READ
		User retrievedUser = userService.getByUsername("johndoe");
		assertNotNull(retrievedUser, "User should be retrievable after creation");
		assertEquals("John Doe", retrievedUser.getFullname(), "Full name should match");
		assertEquals(CustomerType.REGULAR, retrievedUser.getCustomerType(), "Customer type should match");
		assertEquals(5, retrievedUser.getLoanLimit(), "Loan limit should match");

		// UPDATE
		retrievedUser.setFullname("Jonathan Doe");
		retrievedUser.setLoanLimit(10);
		userService.updateUser(retrievedUser);

		User updatedUser = userService.getByUsername("johndoe");
		assertNotNull(updatedUser, "Updated user should be retrievable");
		assertEquals("Jonathan Doe", updatedUser.getFullname(), "Updated full name should match");
		assertEquals(10, updatedUser.getLoanLimit(), "Updated loan limit should match");

		// DELETE
		boolean deleted = userService.deleteUser("johndoe");
		assertTrue(deleted, "User should be deleted successfully");

		User deletedUser = userService.getByUsername("johndoe");
		assertNull(deletedUser, "Deleted user should not be retrievable");
	}
}
