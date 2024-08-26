package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.shopping.api.model.Cart;
import com.shopping.api.model.Product;
import com.shopping.api.stub.CartStubBuilder;
import com.shopping.api.stub.ProductStubBuilder;

public class CartTest {

    @Test
    void idConstructorShouldGenerateSameCartWithId() {
        var cart = CartStubBuilder.builder().build();
        var copy = new Cart(UUID.randomUUID(), cart);
        assertEquals(cart.getProducts(), copy.getProducts());
        assertTrue(cart.getId().isEmpty());
        assertTrue(copy.getId().isPresent());
    }

    @Test
    void productListShouldBeImmutable() {
        var cart = CartStubBuilder.builder().build();
        assertThrows(UnsupportedOperationException.class, () -> cart.getProducts().clear());
    }

    @Test
    void shouldAddProduct() {
        var cart = CartStubBuilder.builder()
                .products(Collections.emptyList()).build();
        assertTrue(cart.getProducts().isEmpty());

        var products = Stream.generate(() -> ProductStubBuilder.builder().build())
                .limit(3).collect(Collectors.toSet());

        var cartWithProducts = cart;
        for (Product product : products) {
            cartWithProducts = cartWithProducts.addProduct(product);
        }

        assertEquals(new HashSet<>(cartWithProducts.getProducts()), products);
    }
}
