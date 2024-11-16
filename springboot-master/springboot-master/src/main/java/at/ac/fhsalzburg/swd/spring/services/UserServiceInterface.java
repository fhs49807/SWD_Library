package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;

import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.User;

public interface UserServiceInterface {

	public abstract String doSomething();

	// create customer
	public abstract boolean addUser(String firstName, String lastName, String eMail, String Tel, Date BirthDate,
			String password, String role, User.CustomerType customerType, int loanLimit);

	public abstract boolean addUser(User user);

	public abstract Collection<User> getAll();

	public abstract boolean hasCredit(User user);

	public abstract boolean deleteUser(String username);

	// search for customer by name
	public abstract User getByUsername(String username);

	// retrieve customer by id
	// public abstract User findById(Long id);

	// retrieve age of customer
	public abstract int getAge(User user);

	// check if customer has reached loan limit
	public abstract boolean canLoanMore(User user);

	// check if customer has outstanding fees
	public abstract boolean hasOutstandingFees(User user);
}
