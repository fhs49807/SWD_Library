package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.EditionService;
import at.ac.fhsalzburg.swd.spring.services.EditionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReserveMediaTransactionServiceTest {

    @Mock
    private EditionServiceInterface editionService;

    @Mock
    private ReserveMediaTransactionRepository reserveMediaTransactionRepository;

    @InjectMocks
    private ReserveMediaTransactionService reserveMediaTransactionService;

    @BeforeEach
    void setupServices() {
        editionService = mock(EditionService.class);

        Media media = new Media();
        Edition edition = new Edition(media, false, null);
        when(editionService.findByMediaAndAvailable(any(Media.class))).thenReturn(Collections.singletonList(edition));

        reserveMediaTransactionRepository = mock(ReserveMediaTransactionRepository.class);
    }

    @Test
    void reserveMediaForCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");

        Media media = new Media();
        media.setName("Dune");


        // customer must reserve the media
        reserveMediaTransactionService.reserveMediaForCustomer(customer, media);
    }
}
