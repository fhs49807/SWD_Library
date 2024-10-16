package at.ac.fhsalzburg.swd.spring.services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.Product;
import at.ac.fhsalzburg.swd.spring.repository.ProductRepository;


@Service
public class ProductService implements ProductServiceInterface {


    @Autowired
    private ProductRepository repo;

    public ProductService() {

    }


    @Override
    public boolean addProduct(String name, float price) {

        if (name != null && name.length() > 0) {
            Product newProduct = new Product(name, price);

            repo.save(newProduct);
            return true;
        }

        return false;

    }

    @Override
    public boolean addProduct(Product product) {

        repo.save(product);

        return false;

    }

    @Override
    public Collection<Product> getAll() {
        List<Product> result = new ArrayList<Product>();

        repo.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Product getById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
