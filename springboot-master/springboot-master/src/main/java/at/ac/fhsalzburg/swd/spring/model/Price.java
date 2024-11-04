package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Price {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY) // oder eine andere Strategie
	 private Long id;

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