package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.enums.CustomerType;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

		User user = new User();
		user.setCredit(100L);

		// Erstelle eine Mock-Transaktion mit einer verspäteten Rückgabe
		MediaTransaction transaction = new MediaTransaction(
			LocalDate.now(),
			LocalDate.now().plusDays(2),
			LocalDate.now().plusDays(1), // expirationDate (vor 1 Tag abgelaufen)
			edition, user, null, null, TransactionStatus.LOANED
		);

		// Mock Repository Verhalten
		when(mediaTransactionRepository.findById(any())).thenReturn(Optional.of(transaction));

		// Mock Invoice Service
		Mockito.doNothing().when(invoiceService).deductAmount(Mockito.any(), Mockito.any());

		// Führe Rückgabeoperation aus
		mediaTransactionService.returnMedia(1L);

		// Überprüfe, dass der Transaktionsstatus auf "COMPLETED" gesetzt wurde
		assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());

		// Überprüfe, dass die Deduktionsmethode des InvoiceService aufgerufen wurde
		verify(invoiceService, times(1)).deductAmount(Mockito.any(), Mockito.any());
	}

	@Test
	void reserveMediaForCustomer() {
		User user = new User();
		user.setCustomerType(CustomerType.STUDENT);
		when(userService.getByUsername(any())).thenReturn(user);

		Media media = new Media();
		media.setName("Dune");
		when(mediaService.findById(any())).thenReturn(media);

		List<Edition> editions = new ArrayList<>();
		editions.add(new Edition());
		when(editionService.findAvailableEditions(any(), any(), any())).thenReturn(editions);

		when(mediaTransactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

		// testing #reserveMediaForCustomer
		LocalDate today = LocalDate.now();
		LocalDate reserveStartDate = today.plusDays(1);
		LocalDate reserveEndDate = today.plusDays(2);
		MediaTransaction persistedTransaction =
			mediaTransactionService.reserveMediaForCustomer("John", 1L, reserveStartDate, reserveEndDate);

		assertNull(persistedTransaction.getStart_date());
		assertNull(persistedTransaction.getEnd_date());
		assertEquals(editions.get(0), persistedTransaction.getEdition());
		assertEquals(user, persistedTransaction.getUser());
		assertEquals(reserveStartDate, persistedTransaction.getReserveStartDate());
		assertEquals(reserveEndDate, persistedTransaction.getReserveEndDate());
		assertEquals(TransactionStatus.RESERVED, persistedTransaction.getStatus());
	}

	@Test
	void loanMediaForCustomer() {
		when(mediaService.findById(any())).thenReturn(new Media());

		List<Edition> editions = new ArrayList<>();
		editions.add(new Edition());
		when(editionService.findAvailableEditions(any(), any(), any())).thenReturn(editions);

		User user = new User();
		user.setCustomerType(CustomerType.STUDENT);
		when(userService.getByUsername(any())).thenReturn(user);

		when(mediaTransactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

		// testing #loanMedia
		LocalDate today = LocalDate.now();
		LocalDate endDate = today.plusDays(1);
		MediaTransaction persistedTransaction = mediaTransactionService.loanMedia("John", 1L, endDate);

		assertEquals(today, persistedTransaction.getStart_date());
		assertEquals(endDate, persistedTransaction.getEnd_date());
		assertEquals(editions.get(0), persistedTransaction.getEdition());
		assertEquals(user, persistedTransaction.getUser());
		assertNull(persistedTransaction.getReserveStartDate());
		assertNull(persistedTransaction.getReserveEndDate());
		assertEquals(TransactionStatus.LOANED, persistedTransaction.getStatus());
	}
}
