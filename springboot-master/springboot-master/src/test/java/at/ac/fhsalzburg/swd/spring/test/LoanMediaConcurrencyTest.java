package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.EditionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.InvoiceServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanMediaConcurrencyTest {

	private MediaTransactionService mediaTransactionService;
	private MediaServiceInterface mediaService;
	private EditionService editionService;
	private UserServiceInterface userService;
	private MediaTransactionRepository mediaTransactionRepository;
	private InvoiceServiceInterface invoiceService;

	@BeforeEach
	public void setUp() {
		mediaService = mock(MediaServiceInterface.class);
		editionService = mock(EditionService.class);
		userService = mock(UserServiceInterface.class);
		mediaTransactionRepository = mock(MediaTransactionRepository.class);
		invoiceService = mock(InvoiceServiceInterface.class);

		mediaTransactionService = new MediaTransactionService(mediaTransactionRepository, mediaService, editionService,
				invoiceService, userService);
	}

	@Test
	public void testLoanMediaConcurrency() throws InterruptedException {
		// Mocking Media, Edition, and User
		Long mediaId = 1L;
		LocalDate endDate = LocalDate.now().plusDays(7);
		Media media = new Media();
		media.setId(mediaId);
		media.setName("Test Media");

		Edition edition = new Edition();
		edition.setId(1L);
		edition.setMedia(media);

		User user = new User();
		user.setUsername("testUser");

		// Mock behavior of dependencies
		when(mediaService.findById(mediaId)).thenReturn(media);

		// Ensure the user is fetched each time
		when(userService.getByUsername(anyString())).thenAnswer(invocation -> {
			String username = invocation.getArgument(0);
			User newUser = new User();
			newUser.setUsername(username);
			return newUser;
		});

		// Simulate only one available edition
		List<Edition> availableEditions = new ArrayList<>();
		availableEditions.add(edition);

		when(editionService.findAvailableEditions(media, LocalDate.now(), endDate)).thenAnswer(invocation -> {
			synchronized (this) {
				if (!availableEditions.isEmpty()) {
					return List.of(availableEditions.remove(0));
				} else {
					return new ArrayList<>();
				}
			}
		});

		// Mock repository save behavior
		when(mediaTransactionRepository.save(any(MediaTransaction.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		// Use threads to simulate concurrency
		int numberOfThreads = 5;
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(1); // To start all threads at the same time
		List<Future<Boolean>> results = new ArrayList<>();

		for (int i = 0; i < numberOfThreads; i++) {
			String username = "user" + i;
			results.add(executor.submit(() -> {
				latch.await(); // Wait for all threads to be ready
				try {
					mediaTransactionService.loanMedia(username, mediaId, endDate);
					return true; // Loan succeeded
				} catch (Exception e) {
					return false; // Loan failed
				}
			}));
		}

		// Release all threads simultaneously
		latch.countDown();
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		// Count successes and failures
		int successCount = 0;
		int failureCount = 0;

		for (Future<Boolean> result : results) {
			try {
				if (result.get()) {
					successCount++;
				} else {
					failureCount++;
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
				failureCount++;
			}
		}

		// Assertions
		assertEquals(1, successCount, "Only one user should successfully loan the media.");
		assertEquals(numberOfThreads - 1, failureCount, "All other users should fail to loan the media.");

		// Verify interactions
		verify(mediaService, times(numberOfThreads)).findById(mediaId);
		verify(editionService, times(numberOfThreads)).findAvailableEditions(media, LocalDate.now(), endDate);
		verify(userService, times(numberOfThreads)).getByUsername(anyString());
		verify(mediaTransactionRepository, times(1)).save(any(MediaTransaction.class));
	}

}
