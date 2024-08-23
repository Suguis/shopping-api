package com.shopping.api.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.shopping.api.model.Cart;

@Repository
public class CartRepository extends InMemoryRepository<UUID, Cart> {
    public Cart create(Cart cart) {
        var cartWithId = new Cart(UUID.randomUUID(), cart);
        create(cartWithId.getId(), cartWithId);
        return cartWithId;
    }
}
