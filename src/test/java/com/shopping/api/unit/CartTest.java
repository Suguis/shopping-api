package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.shopping.api.model.Cart;
import com.shopping.api.stub.CartStubBuilder;

public class CartTest {

    @Test
    void idConstructorGeneratesSameCartWithId() {
        var cart = CartStubBuilder.builder().build();
        var copy = new Cart(UUID.randomUUID(), cart);
        assertEquals(cart.getProducts(), copy.getProducts());
        assertNull(cart.getId());
        assertNotNull(copy.getId());
    }
}
