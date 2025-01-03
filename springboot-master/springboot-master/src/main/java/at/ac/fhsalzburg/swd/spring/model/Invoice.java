package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

//invoice = 

@Entity
@Table(name = "INVOICES")
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;

	private boolean paymentStatus;// TODO: enum?

	// sum of all individual transaction amounts
	private double totalAmount;

	//TODO: add to class diagram
	// one customer can have several invoices
	@ManyToOne
	private User user;

	// one invoice can have multiple transactions
	@OneToMany
	private List<MediaTransaction> transactions;

	public Invoice(Date dueDate, boolean paymentStatus, List<MediaTransaction> transactions, User user) {
		this.dueDate = dueDate;
		this.paymentStatus = paymentStatus;
		this.transactions = transactions;
		this.user = user;
		this.totalAmount = calculateTotalAmount();
	}

	// Calculate total amount based on the price of all editions in each transaction
	public double calculateTotalAmount() {
	    return transactions.stream()
	            .mapToDouble(transaction -> transaction.getEdition().getMedia().getPrice())
	            .sum();
	}


	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getId() {
		return id;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public User getCustomer() {
		return user;
	}

	public void setCustomer(User user) {
		this.user = user;
	}
}