package at.ac.fhsalzburg.swd.spring.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import at.ac.fhsalzburg.swd.spring.controller.TemplateController;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;

@WebMvcTest(TemplateController.class)
class LoanMediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MediaService mediaService;

    @MockBean
    private MediaTransactionService mediaTransactionService;

    @MockBean
    private UserServiceInterface userService;

    @Test
    void testLoanMediaWithValidInput() throws Exception {
        // Mock dependencies
        User mockUser = new User();
        mockUser.setUsername("john");
        MediaTransaction mockTransaction = new MediaTransaction();
        mockTransaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        Media mockMedia = new Media();
        mockMedia.setName("Dune");
        mockMedia.setId(7L);

        when(userService.getByUsername("john")).thenReturn(mockUser);
        when(mediaService.findById(7L)).thenReturn(mockMedia);
        when(mediaTransactionService.loanMedia(eq("john"), eq(7L), any(Date.class)))
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
    void testLoanMediaWithMissingMediaId() throws Exception {
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
