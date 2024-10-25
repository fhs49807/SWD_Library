package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class MediaTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transactionID;

	private String transactionStatus;
	private Date expectedReturnDate;
	private Date expirationDate;
	private Date transactionDate;

	@ManyToOne
	private Edition edition;

	// TODO: @ManyToOne Customer??
	@ManyToOne
	private Customer customer;

	public MediaTransaction(int transactionID, String transactionStatus, Date expectedReturnDate, Date expirationDate,
			Date transactionDate, Edition edition, Customer customer) {
		super();
		this.transactionID = transactionID;
		this.transactionStatus = transactionStatus;
		this.expectedReturnDate = expectedReturnDate;
		this.expirationDate = expirationDate;
		this.transactionDate = transactionDate;
		this.edition = edition;
		this.customer = customer;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
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

}
