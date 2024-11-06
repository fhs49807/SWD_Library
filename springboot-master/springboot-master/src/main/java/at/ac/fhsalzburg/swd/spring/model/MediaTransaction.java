package at.ac.fhsalzburg.swd.spring.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "TRANSACTIONS")
public class MediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private TransactionStatus status;
	private ReturnCondition condition;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expectedReturnDate;// transactionDate + days Loaned for

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expirationDate;// date when loan expires and penalties apply

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date transactionDate;// date when item was loaned or reserved

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date returnDate; // date when item was actually returned

	@ManyToMany
	private Collection<Edition> editions;// one transaction can have multiple items

	@ManyToOne
	private Customer customer;

	public MediaTransaction(Long id, TransactionStatus status, ReturnCondition condition, Date expectedReturnDate,
			Date expirationDate, Date transactionDate, Date returnDate, Collection<Edition> editions,
			Customer customer) {
		super();
		this.id = id;
		this.status = status;
		this.condition = condition;
		this.expectedReturnDate = expectedReturnDate;
		this.expirationDate = expirationDate;
		this.transactionDate = transactionDate;
		this.returnDate = returnDate;
		this.editions = editions;
		this.customer = customer;
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

	public Date getExpectedReturnDate() {
		return expectedReturnDate;
	}

	public void setExpectedReturnDate(Date expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Collection<Edition> getEditions() {
		return editions;
	}

	public void setEditions(Collection<Edition> editions) {
		this.editions = editions;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public enum TransactionStatus {
		ACTIVE, COMPLETED, OVERDUE
	}

	public enum ReturnCondition {
		GOOD, DAMAGED
	}

}
