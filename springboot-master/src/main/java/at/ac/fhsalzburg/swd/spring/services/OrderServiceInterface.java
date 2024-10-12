package at.ac.fhsalzburg.swd.spring.services;

import java.util.Date;

import at.ac.fhsalzburg.swd.spring.model.Order;
import at.ac.fhsalzburg.swd.spring.model.Product;
import at.ac.fhsalzburg.swd.spring.model.Customer;

public interface OrderServiceInterface {

    public abstract Order addOrder(Date date, Customer customer, Iterable<Product> products);

    public abstract Iterable<Order> getAll();

    public abstract Order getById(Long id);

    public abstract void deleteById(Long id);

}
