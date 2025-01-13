package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "RESERVEMEDIATRANSACTION")
public class ReserveMediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne
	private Edition edition;

	private LocalDate reserveStartDate;

	private LocalDate reserveEndDate;

	public ReserveMediaTransaction(User user, Edition edition, LocalDate reserveStartDate, LocalDate reserveEndDate) {
		this.user = user;
		this.edition = edition;
		this.reserveStartDate = reserveStartDate;
		this.reserveEndDate = reserveEndDate;
	}

	public ReserveMediaTransaction() {

	}

	public Edition getEdition() {
		return edition;
	}

	public LocalDate getReserveEndDate() { return reserveEndDate; }

	public Long getId() { return id; }

	public LocalDate getReserveStartDate() { return reserveStartDate; }
}
