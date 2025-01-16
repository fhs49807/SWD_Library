package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import at.ac.fhsalzburg.swd.spring.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MediaTransactionServiceTest {

	@Mock
	private MediaTransactionRepository mediaTransactionRepository;

	@Mock
	private MediaServiceInterface mediaService;

	@Mock
	private EditionServiceInterface editionService;

	@Mock
	private InvoiceServiceInterface invoiceService; // Mock für InvoiceService hinzufügen

	@Mock
	private UserServiceInterface userService;

	@InjectMocks
	private MediaTransactionService mediaTransactionService;

	@Test
	public void testReturnMediaWithLateReturn() {
		// Erstelle eine verspätete Rückgabe-Edition
		Edition edition = new Edition();
		edition.setAvailable(false);

		User user = new User();
		user.setCredit(100L);

		// Erstelle eine Mock-Transaktion mit einer verspäteten Rückgabe
		MediaTransaction transaction = new MediaTransaction(
			new Date(),
			new Date(), // TODO end_date is at least 1 day after start_date
			new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), // expirationDate (vor 1 Tag abgelaufen)
			edition, user, null, null, MediaTransaction.TransactionStatus.ACTIVE
		);

		// Mock Repository Verhalten
		when(mediaTransactionRepository.findById(any())).thenReturn(Optional.of(transaction));

		// Mock Invoice Service
		Mockito.doNothing().when(invoiceService).deductAmount(Mockito.any(), Mockito.any());

		// Führe Rückgabeoperation aus
		mediaTransactionService.returnMedia(1L);

		// Überprüfe, dass die Edition als verfügbar markiert wurde
		assertTrue(transaction.getEdition().isAvailable());

		// Überprüfe, dass der Transaktionsstatus auf "COMPLETED" gesetzt wurde
		assertEquals(MediaTransaction.TransactionStatus.COMPLETED, transaction.getStatus());

		// Überprüfe, dass die Deduktionsmethode des InvoiceService aufgerufen wurde
		verify(invoiceService, times(1)).deductAmount(Mockito.any(), Mockito.any());
	}

	@Test
	void reserveMediaForCustomer() {
		User user = new User();
		user.setCustomerType(User.CustomerType.STUDENT);
		when(userService.getByUsername(any())).thenReturn(user);

		Media media = new Media();
		media.setName("Dune");
		when(mediaService.findById(any())).thenReturn(media);

		List<Edition> editions = new ArrayList<>();
		editions.add(new Edition());
		when(editionService.findAvailableForReserve(any(), any(), any())).thenReturn(editions);

		when(mediaTransactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

		// testing #reserveMediaForCustomer
		LocalDate today = LocalDate.now();
		LocalDate reserveStartDate = today.plusDays(1);
		LocalDate reserveEndDate = today.plusDays(2);
		MediaTransaction persistedTransaction =
			mediaTransactionService.reserveMediaForCustomer("John", 1L, reserveStartDate, reserveEndDate);

		assertNull(persistedTransaction.getStart_date());
		assertNull(persistedTransaction.getEnd_date());
		//assertEquals(today.plusDays(43), persistedTransaction.getLast_possible_return_date());
		assertEquals(editions.get(0), persistedTransaction.getEdition());
		assertEquals(user, persistedTransaction.getUser());
		assertEquals(reserveStartDate, persistedTransaction.getReserveStartDate());
		assertEquals(reserveEndDate, persistedTransaction.getReserveEndDate());
		assertEquals(MediaTransaction.TransactionStatus.RESERVED, persistedTransaction.getStatus());
	}

	@Test
	void loanMediaForCustomer() {
		when(mediaService.findById(any())).thenReturn(new Media());

		List<Edition> editions = new ArrayList<>();
		editions.add(new Edition());
		when(editionService.findByMediaAndAvailable(any())).thenReturn(editions);

		User user = new User();
		user.setCustomerType(User.CustomerType.STUDENT);
		when(userService.getByUsername(any())).thenReturn(user);

		when(mediaTransactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

		// testing #loanMedia
		LocalDate endDate = LocalDate.now().plusDays(1);
		MediaTransaction persistedTransaction = mediaTransactionService.loanMedia("John", 1L, endDate);

		assertEquals(new Date().getDate(), persistedTransaction.getStart_date().getDate());
		assertEquals(endDate, DateUtils.getLocalDateFromDate(persistedTransaction.getEnd_date()));
		//assertEquals(today.plusDays(43), persistedTransaction.getLast_possible_return_date());
		assertEquals(editions.get(0), persistedTransaction.getEdition());
		assertEquals(user, persistedTransaction.getUser());
		assertNull(persistedTransaction.getReserveStartDate());
		assertNull(persistedTransaction.getReserveEndDate());
		assertEquals(MediaTransaction.TransactionStatus.ACTIVE, persistedTransaction.getStatus());
	}
}
