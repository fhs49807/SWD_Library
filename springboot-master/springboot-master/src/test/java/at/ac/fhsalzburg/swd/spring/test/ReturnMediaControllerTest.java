package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.controller.TemplateController;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.InvoiceService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class) // mockitoextension verwendet, um mocks in tests zu unterstützen
@WebMvcTest(TemplateController.class) // @webmvctest testet nur den web-layer (controller)
@ActiveProfiles("test") // legt fest, dass "test"-profile verwendet werden sollen
public class ReturnMediaControllerTest {

	@Autowired
	private MockMvc mockMvc; // mocking der spring mvc schnittstelle zur simulation von http anfragen

	// erstellt einen mock für den user service, um benutzerbezogene operationen zu simulieren
	@Mock
	private UserServiceInterface userService;

	@Mock
	private InvoiceService invoiceService;

	// erstellt einen mock für den media transaction service, um transaktionen zu simulieren
	@Mock
	private MediaTransactionServiceInterface mediaTransactionService;

	@InjectMocks
	private TemplateController templateController; // erstellt eine instanz des controllers und injiziert die mocks

	// wird vor jedem test ausgeführt, um die mockmvc instanz zu initialisieren
	@BeforeEach
	void setup() {
		// standalone setup für templatecontroller; stellt sicher, dass mockmvc korrekt konfiguriert ist
		mockMvc = MockMvcBuilders.standaloneSetup(templateController).build();
	}

	@Test // test für das anzeigen der rückgabeseite
	@WithMockUser(username = "john.doe", roles = {"USER"})
	// mitmockuser annotation simuliert einen benutzer mit dem benutzernamen "john.doe" und der rolle "user"
	public void givenAuthenticatedUser_whenGetReturnMediaPage_thenReturnMediaPage() throws Exception {
		// mocking des benutzers und der ausgeliehenen medien
		User mockUser =
			new User("john.doe", "John Doe", "john.doe@example.com", "123456789", new Date(), "pw", "USER", null, null,
				5);
		MediaTransaction transaction = new MediaTransaction();
		transaction.setId(1L); // setzt die id der transaktion auf 1
		transaction.setStart_date(LocalDate.now()); // setzt das transaktionsdatum
		Collection<MediaTransaction> mockLoans =
			Arrays.asList(transaction); // erstellt eine liste mit mock-transaktionen

		when(userService.getByUsername(anyString())).thenReturn(
			mockUser); // mockuser wird zurückgegeben, wenn der userService getByUsername aufgerufen wird

		when(mediaTransactionService.findLoansByUser(mockUser)).thenReturn(
			mockLoans); // mockloans werden zurückgegeben, wenn mediaTransactionService findLoansByUser aufgerufen wird

		// ausführung der http-anfrage und überprüfung der view und der modellattribute
		mockMvc.perform(get("/returnMedia")) // simuliert einen http-get auf die url "/returnMedia"
			.andExpect(status().isOk()) // erwartet statuscode 200 (ok)
			.andExpect(view().name("returnMedia")) // erwartet, dass die view "returnMedia" verwendet wird
			.andExpect(model().attributeExists("loans")); // erwartet, dass das modellattribut "loans" existiert
	}

	@Test // test für das zurückgeben von medien
	@WithMockUser(username = "john.doe", roles = {"USER"})
	// simuliert einen benutzer mit benutzernamen "john.doe" und der rolle "user"
	public void givenTransactionId_whenPostReturnMedia_thenRedirectToReturnMediaPage() throws Exception {
		// mocking des rückgabeprozesses
		Mockito.doNothing().when(mediaTransactionService)
			.returnMedia(Mockito.anyLong()); // mocking der rückgabe-funktion

		// sende eine post-anfrage, um medien zurückzugeben
		mockMvc.perform(post("/returnMedia") // simuliert eine http-post-anfrage auf "/returnMedia"
				.param("transactionId", "1")) // setzt den parameter "transactionId" auf "1"
			.andExpect(status().is3xxRedirection()) // erwartet einen umleitungsstatus (3xx)
			.andExpect(redirectedUrl("/returnMedia")); // erwartet eine umleitung zur url "/returnMedia"
	}

	@Test
	@WithMockUser(username = "john.doe", roles = {"USER"})
	// simuliert einen benutzer mit dem benutzernamen "john.doe" und der rolle "USER"
	public void givenLateReturn_whenReturnMedia_thenInvoiceCreated() throws Exception {
		MediaTransaction transaction = new MediaTransaction(); // erstelle eine neue instanz von media transaction
		transaction.setId(1L); // setze die id der transaktion auf 1
		transaction.setStart_date(LocalDate.now()); // setze das transaktionsdatum auf das aktuelle datum

		LocalDate expirationDate = LocalDate.now().minusDays(2);  // 2 tage verspätet
		transaction.setLast_possible_return_date(expirationDate); // setze das fälligkeitsdatum der transaktion
		transaction.setStatus(
			MediaTransaction.TransactionStatus.ACTIVE); // setze den status der transaktion auf "aktiv"
		transaction.setReturnDate(LocalDate.now());  // setze das rückgabedatum auf heute

		User mockUser =
			new User("john.doe", "John Doe", "john.doe@example.com", "123456789", new Date(), "pw", "USER", null, null,
				5); // erstelle einen mock-benutzer
		transaction.setUser(mockUser); // setze den benutzer für diese transaktion

		doNothing().when(mediaTransactionService).returnMedia(Mockito.anyLong()); // mocke den rückgabeprozess
		Mockito.doNothing().when(invoiceService).deductAmount(Mockito.any(User.class),
			Mockito.any(MediaTransaction.class)); // mocke die berechnung der rechnung

		double expectedPenalty = 2.0;  // beispiel für 2 tage verspätung à 1 euro pro tag

		mediaTransactionService.returnMedia(1L); // führe die rückgabe der transaktion aus

		verify(invoiceService, times(1)).deductAmount(mockUser,
			transaction); // stelle sicher, dass deductAmount genau einmal aufgerufen wurde
	}

	// Test for access restriction when not logged in
	@Test
	public void givenNoAuth_whenAccessReturnMedia_thenShowLoginPage() throws Exception {
		// Perform GET request for /returnMedia with no authentication
		mockMvc.perform(get("/returnMedia"))
			.andExpect(status().isOk()) // should return status 200
			.andExpect(view().name("login")) // should show the login page
			.andExpect(model().attributeExists("errorMessage")) // check if the errorMessage is set
			.andExpect(model().attribute("errorMessage",
				"You must log in to access this page.")); // check for the correct error message
	}
}
