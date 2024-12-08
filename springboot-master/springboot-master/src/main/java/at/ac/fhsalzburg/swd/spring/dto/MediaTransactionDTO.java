package at.ac.fhsalzburg.swd.spring.dto;

import java.time.LocalDate;

public record MediaTransactionDTO(Long id, String mediaName, LocalDate expirationDate) {
}
