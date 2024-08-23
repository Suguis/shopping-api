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
import com.shopping.api.stub.ProductStubBuilder;

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
        var product = ProductStubBuilder.builder().build();
        productRepository.create(product);
    }

    @Test
    void shouldCreateAndGetSame() {
        var product = ProductStubBuilder.builder().build();
        productRepository.create(product);
        var retrieved = productRepository.getByKey(product.getId()).orElseThrow();

        assertEquals(product, retrieved);
    }

    @Test
    void shouldNotFindUnexistingProduct() {
        var retrieved = productRepository.getByKey(1L);

        assertEquals(Optional.empty(), retrieved);
    }

    @Test
    void shouldReturnDeletedProductsOnDeleteAll() {
        var products = Set.of(
                ProductStubBuilder.builder().build(),
                ProductStubBuilder.builder().build());

        products.forEach(product -> productRepository.create(product));

        var retrieved = new HashSet<>(productRepository.deleteAll().values());

        assertEquals(products, retrieved);
    }

    @Test
    void shouldNotGetAfterDeletingAll() {
        var product = ProductStubBuilder.builder().build();

        productRepository.create(product);

        productRepository.deleteAll();

        var retrieved = productRepository.getByKey(product.getId());

        assertEquals(Optional.empty(), retrieved);
    }
}
