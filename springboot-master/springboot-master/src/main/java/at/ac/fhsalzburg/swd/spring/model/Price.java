package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Price {

	private double amount;

	@ManyToOne
	private User user;

	// TODO: generate constructor including user?? and amount
	public Price(User user, double amount) {
		this.user = user;
		this.amount = amount;
	}

	// TODO: generate getters and setters
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}