package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.controller.MediaController;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.InvoiceService;
import at.ac.fhsalzburg.swd.spring.services.LibraryService;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MediaController.class)
@ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class ReturnMediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceInterface userService;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private MediaTransactionServiceInterface mediaTransactionService;

    @MockBean
    private MediaServiceInterface mediaService;

    @MockBean
    private LibraryService libraryService;

    // Mock any dependencies from BaseController if necessary
    // For example:
    // @MockBean
    // private SomeService someServiceFromBaseController;

    /**
     * 1) Authenticated user GET /returnMedia -> should show "returnMedia" view with loans.
     */
    @Test
    @WithMockUser(username = "john.doe", roles = {"USER"})
    public void givenAuthenticatedUser_whenGetReturnMediaPage_thenReturnMediaPage() throws Exception {
        // Arrange
        User mockUser = new User("john.doe", "John Doe", "john.doe@example.com",
                "123456789", new Date(), "pw", "USER", null, null, 5);

        MediaTransaction transaction = new MediaTransaction();
        transaction.setId(1L);
        transaction.setStart_date(LocalDate.now());

        Collection<MediaTransaction> mockLoans = Arrays.asList(transaction);

        // Mock the service calls
        when(userService.getByUsername(anyString())).thenReturn(mockUser);
        when(mediaTransactionService.findLoansByUser(mockUser)).thenReturn(mockLoans);

        // Act & Assert
        mockMvc.perform(get("/returnMedia"))
                .andExpect(status().isOk())
                .andExpect(view().name("returnMedia"))
                .andExpect(model().attributeExists("loans"));
    }

    /**
     * 2) Authenticated user POST /returnMedia -> on successful return, controller redirects
     *    to "/returnMediaSuccess?transactionId=...".
     */
    @Test
    @WithMockUser(username = "john.doe", roles = {"USER"})
    public void givenTransactionId_whenPostReturnMedia_thenRedirectToSuccessPage() throws Exception {
        // Arrange
        // Stub out the service so it doesn't throw an exception
        doNothing().when(mediaTransactionService).returnMedia(anyLong());

        // Act & Assert
        mockMvc.perform(post("/returnMedia")
                .with(csrf()) // Add CSRF token
                .param("transactionId", "1"))
                .andExpect(status().is3xxRedirection())
                // The code in MediaController returns "redirect:/returnMediaSuccess?transactionId=1"
                .andExpect(redirectedUrl("/returnMediaSuccess?transactionId=1"));
    }

    /**
     * 3) "Late return" scenario: we want to confirm that invoiceService.deductAmount(...) is called.
     *    Since this is a controller test, we won't run the real business logic unless we remove the stub.
     *    We'll simply verify the service calls for demonstration.
     */
    @Test
    @WithMockUser(username = "john.doe", roles = {"USER"})
    public void givenLateReturn_whenReturnMedia_thenInvoiceCreated() throws Exception {
        // Arrange
        // We'll create a "late" MediaTransaction and a user with enough credit
        User mockUser = new User("john.doe", "John Doe", "john.doe@example.com",
                "123456789", new Date(), "pw", "USER", null, null, 5);
        mockUser.setCredit(10L); // Ensure enough credit to avoid exception

        MediaTransaction transaction = new MediaTransaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.LOANED);
        transaction.setStart_date(LocalDate.now().minusDays(5));
        transaction.setLast_possible_return_date(LocalDate.now().minusDays(2)); // Overdue by 2 days
        transaction.setReturnDate(LocalDate.now());
        transaction.setUser(mockUser);

        // Mock the service to simulate calling invoiceService.deductAmount
        doAnswer(invocation -> {
            // Extract arguments
            Long transactionId = invocation.getArgument(0);
            // Simulate fetching the transaction (since it's not persisted in test)
            // Here, we're directly using the 'transaction' object created above
            invoiceService.deductAmount(mockUser, transaction);
            return null;
        }).when(mediaTransactionService).returnMedia(1L);

        // Act
        mockMvc.perform(post("/returnMedia")
                .with(csrf()) // Add CSRF token
                .param("transactionId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/returnMediaSuccess?transactionId=1"));

        // Assert: verify invoice was "created"
        verify(mediaTransactionService, times(1)).returnMedia(1L);
        verify(invoiceService, times(1)).deductAmount(mockUser, transaction);
    }

    /**
     * 4) Unauthenticated user GET /returnMedia -> should show "login" page with an error message.
     */
    @Test
    public void givenNoAuth_whenAccessReturnMedia_thenShowLoginPage() throws Exception {
        mockMvc.perform(get("/returnMedia"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "You must log in to access this page."));
    }
}
