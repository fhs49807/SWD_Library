package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;

import java.util.Date;

import org.springframework.stereotype.Repository;

import at.ac.fhsalzburg.swd.spring.model.User;

public interface UserServiceInterface {

	public abstract String doSomething();

	public abstract boolean addUser(String firstName, String lastName, String eMail, String Tel, Date BirthDate,
			String password, String role);

	public abstract boolean addUser(User user);

	public abstract Collection<User> getAll();

	public abstract boolean hasCredit(User user);

	public abstract boolean deleteUser(String username);

	public abstract User getByUsername(String username);

}
