package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.controller.TemplateController;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TemplateController.class)
public class LoanMediaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MediaService mediaService;

	@MockBean
	private MediaTransactionService mediaTransactionService;

	@MockBean
	private UserServiceInterface userService;

	@Test
	public void testLoanMediaWithValidInput() throws Exception {
		// Mock dependencies
		User mockUser = new User();
		mockUser.setUsername("john");
		MediaTransaction mockTransaction = new MediaTransaction();
		mockTransaction.setStatus(TransactionStatus.LOANED);

		Media mockMedia = new Media();
		mockMedia.setName("Dune");
		mockMedia.setId(7L);

		when(userService.getByUsername("john")).thenReturn(mockUser);
		when(mediaService.findById(7L)).thenReturn(mockMedia);
		when(mediaTransactionService.loanMedia(eq("john"), eq(7L), any(LocalDate.class)))
			.thenReturn(mockTransaction);

		// Perform request
		mockMvc.perform(post("/loanMedia")
				.param("mediaId", "7")
				.param("loanDate", "2024-11-29T17:00")
				.sessionAttr("SPRING_SECURITY_CONTEXT", createAuthentication("john")))
			.andExpect(status().isOk())
			.andExpect(model().attribute("mediaTitle", "Dune"))
			.andExpect(view().name("loanSuccess"));
	}

	@Test
	public void testLoanMediaWithMissingMediaId() throws Exception {
		mockMvc.perform(post("/loanMedia")
				.param("loanDate", "2024-11-29T17:00")
				.sessionAttr("SPRING_SECURITY_CONTEXT", createAuthentication("john")))
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", "Please select a media item and specify the loan date."))
			.andExpect(view().name("loan"));
	}

	private SecurityContext createAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(username, "password"));
		return context;
	}
}
