package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanMediaTest {

	@InjectMocks
	private MediaTransactionService mediaTransactionService;

	@Mock
	private MediaTransactionRepository mediaTransactionRepository;

	@Mock
	private MediaServiceInterface mediaService;

	@Mock
	private EditionServiceInterface editionService;

	@Mock
	private UserServiceInterface userService;

	@Mock
	private InvoiceServiceInterface invoiceService;

	private User testUser;
	private Media testMedia;
	private Edition testEdition;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Create a mock user
		testUser = new User("testUser", "Test User", "test@example.com", "123456", new Date(), "password", "ROLE_USER",
				null, User.CustomerType.REGULAR, 5);

		// Create a mock media and edition
		testMedia = new Media("Test Media", null, null, null, 0);
		testEdition = new Edition();
		testEdition.setAvailable(true);

		// Mock behaviors
		when(userService.getByUsername("testUser")).thenReturn(testUser);
		when(mediaService.findById(1L)).thenReturn(testMedia);
		when(editionService.findByMediaAndAvailable(testMedia)).thenReturn(Collections.singletonList(testEdition));
		when(editionService.findFirstAvailableEdition(any())).thenReturn(testEdition);
	}

	@Test
	void testLoanMediaSuccessful() {
		MediaTransaction transaction = mediaTransactionService.loanMedia("testUser", 1L, new Date());

		assertNotNull(transaction, "Transaction should not be null");
		assertEquals(testUser, transaction.getUser(), "Transaction should be linked to the correct user");
		assertEquals(testEdition, transaction.getEdition(), "Transaction should link to the correct edition");

		verify(editionService).markEditionAsUnavailable(eq(testEdition), any(Date.class));
		verify(mediaTransactionRepository).save(any(MediaTransaction.class));
	}

	@Test
	void testLoanMediaNoAvailableEdition() {
		when(editionService.findByMediaAndAvailable(testMedia)).thenReturn(Collections.emptyList());
		when(editionService.findFirstAvailableEdition(any())).thenReturn(null);

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			mediaTransactionService.loanMedia("testUser", 1L, new Date());
		});

		assertEquals("No available editions.", exception.getMessage());

		verify(editionService, never()).markEditionAsUnavailable(any(Edition.class), any(Date.class));
		verify(mediaTransactionRepository, never()).save(any(MediaTransaction.class));
	}

}
