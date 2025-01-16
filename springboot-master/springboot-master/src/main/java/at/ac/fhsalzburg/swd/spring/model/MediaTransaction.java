package at.ac.fhsalzburg.swd.spring.model;

import at.ac.fhsalzburg.swd.spring.enums.ReturnCondition;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "TRANSACTIONS")
public class MediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TransactionStatus status;

	@Enumerated(EnumType.STRING)
	private ReturnCondition condition;

	private LocalDate start_date;// date when item was loaned or reserved

	private LocalDate end_date;// start_date + days Loaned for (selected end_date)

	// date when loan expires and penalties apply (depends on customer type --> student/regular)
	private LocalDate last_possible_return_date;

	private LocalDate return_date; // date when item was actually returned

	// TODO: add to class diagram
	//	@ManyToMany
	//	private Collection<Media> media;// one transaction can reserve multiple media items

	@ManyToOne
	@JoinColumn(name = "edition_id", nullable = false)
	private Edition edition; // Link a single edition to the transaction

	@ManyToOne
	private User user;

	private LocalDate reserveStartDate;

	private LocalDate reserveEndDate;

	public MediaTransaction() {

	}

	//constructor used to create transaction in MediaTransactionService.java
	public MediaTransaction(LocalDate start_date, LocalDate end_date, LocalDate last_possible_return_date,
		Edition edition, User user, LocalDate reserveStartDate, LocalDate reserveEndDate, TransactionStatus status) {
		this.start_date = start_date;
		this.end_date = end_date;
		this.last_possible_return_date = last_possible_return_date;
		this.edition = edition;
		this.user = user;
		this.reserveStartDate = reserveStartDate;
		this.reserveEndDate = reserveEndDate;
		this.status = status;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public LocalDate getLast_possible_return_date() {
		return last_possible_return_date;
	}

	public void setLast_possible_return_date(LocalDate last_possible_return_date) {
		this.last_possible_return_date = last_possible_return_date;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public LocalDate getReturnDate() {
		return return_date;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.return_date = returnDate;
	}

	public Edition getEdition() {
		return edition;
	}

	public void setEdition(Edition editions) {
		this.edition = editions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	// Setter nur für Testzwecke
	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getReserveEndDate() { return reserveEndDate; }

	public LocalDate getReserveStartDate() { return reserveStartDate; }

}
