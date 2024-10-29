package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String transactionStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expectedReturnDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expirationDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date transactionDate;

	@ManyToOne
	private Edition edition;

	// TODO: @ManyToOne Customer??
	@ManyToOne
	private Customer customer;

	public MediaTransaction(Long id, String transactionStatus, Date expectedReturnDate, Date expirationDate,
			Date transactionDate, Edition edition, Customer customer) {
		super();
		this.id = id;
		this.transactionStatus = transactionStatus;
		this.expectedReturnDate = expectedReturnDate;
		this.expirationDate = expirationDate;
		this.transactionDate = transactionDate;
		this.edition = edition;
		this.customer = customer;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
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

	public Edition getEdition() {
		return edition;
	}

	public void setEdition(Edition edition) {
		this.edition = edition;
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

}
