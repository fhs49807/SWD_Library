package at.ac.fhsalzburg.swd.spring.test;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MediaTransactionServiceTest {

    @Mock
    private MediaTransactionRepository mediaTransactionRepository;

    @Mock
    private EditionRepository editionRepository;

    @InjectMocks
    private MediaTransactionService mediaTransactionService;

    @Test
    public void testReturnMedia() {
        // Mock-Transaktion erstellen
        MediaTransaction transaction = new MediaTransaction(
                new Date(),                     // transactionDate
                new Date(),                     // expirationDate 
                Collections.emptyList(),        // media 
                Collections.emptyList(),        // editions 
                null                            // customer
        );

        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        // mock repository verhalten
        Mockito.<Optional<MediaTransaction>>when(mediaTransactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        // zurückgeben 
        mediaTransactionService.returnMedia(1L);

        // sicherstellen, dass editionen als verfügbar markiert 
        for (Edition edition : transaction.getEditions()) {
            assertTrue(edition.isAvailable());
        }

        // sicherstellen, dass transaktion aktualisiert
        assertEquals(MediaTransaction.TransactionStatus.COMPLETED, transaction.getStatus());
    }
}
