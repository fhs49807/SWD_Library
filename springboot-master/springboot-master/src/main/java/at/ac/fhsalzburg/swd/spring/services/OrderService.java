package at.ac.fhsalzburg.swd.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Order;
import at.ac.fhsalzburg.swd.spring.model.Product;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.OrderRepository;


@Service
public class OrderService implements OrderServiceInterface {

    private final UserServiceInterface userService;

    @Autowired
    private final OrderRepository repo;

    
    public OrderService(UserServiceInterface userService, OrderRepository orderRepo) {
    	this.userService = userService;
    	this.repo = orderRepo;
    }


    @Override
    public Order addOrder(Date date, User customer, Iterable<Product> products) {
        List<Product> productlist = new ArrayList<Product>();
        products.forEach(productlist::add);

        if (userService.hasCredit(customer)) {
            Order order = new Order(date, customer, productlist);
            order = repo.save(order);

            return order;
        } else {
            return null;
        }

    }


    @Override
    public Iterable<Order> getAll() {
        return repo.findAll();
    }

    @Override
    public Order getById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
