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
import at.ac.fhsalzburg.swd.spring.services.InvoiceService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MediaTransactionServiceTest {

    @Mock
    private MediaTransactionRepository mediaTransactionRepository;

    @Mock
    private EditionRepository editionRepository;

    @Mock
    private InvoiceService invoiceService; // Mock für InvoiceService hinzufügen

    @InjectMocks
    private MediaTransactionService mediaTransactionService;

    @Test
    public void testReturnMediaWithLateReturn() {
        // Erstelle eine verspätete Rückgabe-Edition
        Edition edition = new Edition();
        edition.setAvailable(false);

        // Erstelle eine Mock-Transaktion mit einer verspäteten Rückgabe
        MediaTransaction transaction = new MediaTransaction(
                new Date(),                     // transactionDate
                new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24),  // expirationDate (vor 1 Tag abgelaufen)
                edition,                        // edition
                null                            // customer
        );

        transaction.setStatus(MediaTransaction.TransactionStatus.ACTIVE);

        // Mock Repository Verhalten
        Mockito.<Optional<MediaTransaction>>when(mediaTransactionRepository.findById(1L))
                .thenReturn(Optional.of(transaction));

        // Mock Invoice Service
        Mockito.doNothing().when(invoiceService).deductAmount(Mockito.any(), Mockito.any());

        // Führe Rückgabeoperation aus
        mediaTransactionService.returnMedia(1L);

        // Überprüfe, dass die Edition als verfügbar markiert wurde
        assertTrue(transaction.getEdition().isAvailable());

        // Überprüfe, dass der Transaktionsstatus auf "COMPLETED" gesetzt wurde
        assertEquals(MediaTransaction.TransactionStatus.COMPLETED, transaction.getStatus());

        // Überprüfe, dass die Deduktionsmethode des InvoiceService aufgerufen wurde
        verify(invoiceService, times(1)).deductAmount(Mockito.any(), Mockito.any());
    }

}
