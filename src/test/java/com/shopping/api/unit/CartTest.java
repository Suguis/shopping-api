package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shopping.api.model.Cart;
import com.shopping.api.stub.CartStubBuilder;

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
}
