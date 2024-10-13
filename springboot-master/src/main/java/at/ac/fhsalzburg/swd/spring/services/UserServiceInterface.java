package at.ac.fhsalzburg.swd.spring.services;

import java.util.Collection;
import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.Customer;

public interface UserServiceInterface {

    public abstract String doSomething();

    public abstract boolean addUser(String firstName, String lastName, String eMail, String Tel,
            Date BirthDate, String password, String role);

    public abstract boolean addUser(Customer user);

    public abstract Collection<Customer> getAll();

    public abstract boolean hasCredit(Customer user);

    public abstract boolean deleteUser(String username);

    public abstract Customer getByUsername(String username);

}
