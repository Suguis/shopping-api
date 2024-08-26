package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.repository.CartRepository;
import com.shopping.api.service.CartService;
import com.shopping.api.stub.ProductStubBuilder;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void clearDatabase() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetCarts() {
        var cart1 = cartService.create();
        var cart2 = cartService.create();
        var retrieved1 = cartService.get(cart1.getId().get()).get();
        var retrieved2 = cartService.get(cart2.getId().get()).get();
        assertEquals(cart1, retrieved1);
        assertEquals(cart2, retrieved2);
    }

    @Test
    void shouldAddProductsToCart() {
        var cart = cartService.create();

        assertTrue(cart.getProducts().isEmpty());

        var products = Stream.generate(() -> ProductStubBuilder.builder().build())
                .limit(3).collect(Collectors.toSet());

        products.forEach(product -> cartService.addProduct(cart.getId().get(), product));

        var cartWithProducts = cartService.get(cart.getId().get()).get();
        assertEquals(products, new HashSet<>(cartWithProducts.getProducts()));
    }

    @Test
    void shouldDeleteSpecifiedCart() {
        var cart1 = cartService.create();
        var cart2 = cartService.create();
        cartService.delete(cart1.getId().get());

        assertTrue(cartService.get(cart1.getId().get()).isEmpty());
        assertEquals(cart2, cartService.get(cart2.getId().get()).get());
    }

}
