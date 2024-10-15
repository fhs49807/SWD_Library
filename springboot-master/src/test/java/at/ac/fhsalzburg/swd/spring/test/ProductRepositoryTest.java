package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.model.Product;
import at.ac.fhsalzburg.swd.spring.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByUsername_thenReturnCustomer() {
        // create and persist two products into the database
        Product givenProduct = new Product("book1", 10);
        persistProduct(givenProduct);

        Product givenProduct2 = new Product("book1", 5);
        persistProduct(givenProduct2);

        // find products by given name
        List<Product> foundProducts = productRepository.findByName("book1");

        // assert first persisted product
        assertEquals(givenProduct, foundProducts.get(0));

        // check whether the price of second product is correct
        assertEquals(5, foundProducts.get(1).getPrice());
    }

    private void persistProduct(Product product) {
        entityManager.persist(product);
        entityManager.flush();
    }

}
