//package at.ac.fhsalzburg.swd.spring.test;
//
//import at.ac.fhsalzburg.swd.spring.model.Edition;
//import at.ac.fhsalzburg.swd.spring.model.Media;
//import at.ac.fhsalzburg.swd.spring.model.User;
//import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
//import at.ac.fhsalzburg.swd.spring.services.EditionServiceInterface;
//import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionService;
//import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionServiceInterface;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//@ActiveProfiles("test")
//class ReserveMediaTransactionServiceTest {
//
//    @Mock
//    private EditionServiceInterface editionService;
//
//    @Mock
//    private ReserveMediaTransactionRepository reserveMediaTransactionRepository;
//
//    @InjectMocks
//    private ReserveMediaTransactionServiceInterface reserveMediaTransactionService =
//            new ReserveMediaTransactionService();
//
//    private User user;
//
//    @BeforeEach
//    void setup() {
//        user = new User();
//        user.setUsername("John Doe");
//    }
//
//    @Test
//    void reserveMediaForCustomer() {
//        Media media = new Media();
//        media.setName("Dune");
//        when(editionService.findByMediaAndAvailable(media)).thenReturn(Collections.emptyList());
//
//
//        reserveMediaTransactionService.reserveMediaForCustomer(user, "Dune");
//        verify(reserveMediaTransactionRepository).save(any());
//    }
//
//    @Test
//    void loanMediaForCustomer() {
//        Media media = new Media();
//        media.setName("Dune");
//        Edition edition = new Edition(media, true, null);
//        when(editionService.findByMediaAndAvailable(media)).thenReturn(Collections.singletonList(edition));
//
//
//        reserveMediaTransactionService.reserveMediaForCustomer(user, "Dune");
//        verify(reserveMediaTransactionRepository, times(0)).save(any());
//    }
//}
