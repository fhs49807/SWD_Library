package at.ac.fhsalzburg.swd.spring.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRANSACTIONS")
public class MediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	private ReturnCondition condition;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start_date;// date when item was loaned or reserved

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end_date;// start_date + days Loaned for (selected end_date)

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date last_possible_return_date;// date when loan expires and penalties apply (depends on customer type -->
											// student/regular)

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date return_date; // date when item was actually returned

	// TODO: add to class diagram
	@ManyToMany
	private Collection<Media> media;// one transaction can reserve multiple media items

	@ManyToOne
	@JoinColumn(name = "edition_id", nullable = false)
	private Edition edition; // Link a single edition to the transaction

	@ManyToOne
	private User user;

	public MediaTransaction() {

	}

	public MediaTransaction(Date transactionDate, Date expirationDate, Collection<Media> media, Edition edition,
			User user) {
		this.start_date = transactionDate;
		this.last_possible_return_date = expirationDate;
		this.media = media;
		this.edition = edition;
		this.user = user;
		this.status = TransactionStatus.ACTIVE; // default status: ACTIVE
	}

	//constructor used to create transaction in MediaTransactionService.java
	public MediaTransaction(Date transactionDate, Date expirationDate, Edition edition, User user) {
	    this.start_date = transactionDate;
	    this.last_possible_return_date = expirationDate;
	    this.edition = edition;
	    this.user = user;
	    this.status = TransactionStatus.ACTIVE; // Default status
	}


	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public ReturnCondition getCondition() {
		return condition;
	}

	public void setCondition(ReturnCondition condition) {
		this.condition = condition;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getLast_possible_return_date() {
		return last_possible_return_date;
	}

	public void setLast_possible_return_date(Date last_possible_return_date) {
		this.last_possible_return_date = last_possible_return_date;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getReturnDate() {
		return return_date;
	}

	public void setReturnDate(Date returnDate) {
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

	public enum TransactionStatus {
		ACTIVE, COMPLETED, OVERDUE
	}

	public enum ReturnCondition {
		GOOD, DAMAGED
	}

}
