package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.repository.ProductRepository;
import com.shopping.api.stub.ProductStub;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clearDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreate() {
        var product = ProductStub.builder().build();
        productRepository.create(product);
    }

    @Test
    void shouldReturnCreated() {
        var product = ProductStub.builder().build();
        var retrieved = productRepository.create(product);
        assertEquals(product, retrieved);
    }

    @Test
    void shouldCreateAndGetSame() {
        var product = ProductStub.builder().build();
        productRepository.create(product);
        var retrieved = productRepository.getById(product.getId()).orElseThrow();

        assertEquals(product, retrieved);
    }

    @Test
    void shouldNotFindUnexistingProduct() {
        var retrieved = productRepository.getById(1L);

        assertEquals(Optional.empty(), retrieved);
    }

    @Test
    void shouldReturnDeletedProductsOnDeleteAll() {
        var products = Set.of(
                ProductStub.builder().build(),
                ProductStub.builder().build());

        products.forEach(product -> productRepository.create(product));

        var retrieved = new HashSet<>(productRepository.deleteAll());

        assertEquals(products, retrieved);
    }

    @Test
    void shouldNotGetAfterDeletingAll() {
        var product = ProductStub.builder().build();

        productRepository.create(product);

        productRepository.deleteAll();

        var retrieved = productRepository.getById(product.getId());

        assertEquals(Optional.empty(), retrieved);
    }
}
