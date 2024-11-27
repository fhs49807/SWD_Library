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
        // Create a mock Edition
        Edition edition = new Edition();
        edition.setAvailable(false);

        // Create a mock transaction
        MediaTransaction transaction = new MediaTransaction(
                new Date(),                     // transactionDate
                new Date(),                     // expirationDate 
                edition,                        // edition
                null                            // customer
        );

        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        // Mock repository behavior
        Mockito.<Optional<MediaTransaction>>when(mediaTransactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        // Perform the return operation
        mediaTransactionService.returnMedia(1L);

        // Validate that the edition is marked as available
        assertTrue(transaction.getEdition().isAvailable());

        // Validate that the transaction status is updated to COMPLETED
        assertEquals(MediaTransaction.TransactionStatus.COMPLETED, transaction.getStatus());
    }
}
