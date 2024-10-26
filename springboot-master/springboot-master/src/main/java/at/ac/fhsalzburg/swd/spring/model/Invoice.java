package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "Invoices")
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int invoiceID;
	private Date dueDate;
	private boolean paymentStatus;

	@OneToOne
	private Price price;

	public Invoice(int invoiceID, Date dueDate, boolean paymentStatus, Price price) {
		super();
		this.invoiceID = invoiceID;
		this.dueDate = dueDate;
		this.paymentStatus = paymentStatus;
		this.price = price;
	}

	public int getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
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

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

}
