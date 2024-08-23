package com.shopping.api.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.api.model.Cart;
import com.shopping.api.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart create() {
        var cart = cartRepository.create(Cart.builder().build());
        return cart;
    }

    public Optional<Cart> get(UUID id) {
        return cartRepository.getByKey(id);
    }

    public Optional<Cart> delete(UUID id) {
        return cartRepository.deleteByKey(id);
    }

}
