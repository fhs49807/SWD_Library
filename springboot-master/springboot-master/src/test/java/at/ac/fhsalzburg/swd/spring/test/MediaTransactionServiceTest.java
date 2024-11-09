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
                new Date(),                     // expirationDate (als Beispiel)
                Collections.emptyList(),        // media (leere Liste als Beispiel)
                Collections.emptyList(),        // editions (leere Liste als Beispiel)
                null                            // customer
        );

        // Setze den Status der Transaktion
        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        // Mock das Verhalten des Repositories
        Mockito.<Optional<MediaTransaction>>when(mediaTransactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        // Rückgabe durchführen
        mediaTransactionService.returnMedia(1L);

        // Sicherstellen, dass die Editionen als verfügbar markiert wurden
        for (Edition edition : transaction.getEditions()) {
            assertTrue(edition.isAvailable());
        }

        // Sicherstellen, dass die Transaktion aktualisiert wurde
        assertEquals(MediaTransaction.TransactionStatus.COMPLETED, transaction.getStatus());
    }
}
