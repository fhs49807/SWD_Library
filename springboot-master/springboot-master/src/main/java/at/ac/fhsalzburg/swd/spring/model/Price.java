package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Price extends BaseEntity {

	private double amount;

	@ManyToOne
	private Customer customer;

	// TODO: generate constructor including customer?? and amount
	public Price(Customer customer, double amount) {
		this.customer = customer;
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
