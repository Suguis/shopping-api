package com.shopping.api.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.api.model.Cart;
import com.shopping.api.model.Product;
import com.shopping.api.repository.CartRepository;

// TODO: test this service

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

    public boolean isProductAlreadyInTheCart(UUID id, Product product) {
        return get(id).map(cart -> cart.getProducts().stream().anyMatch(p -> p.getId() == product.getId()))
                .orElseThrow(() -> new IllegalArgumentException("id doesn't correspond to any cart"));
    }

    public Cart addProduct(UUID cartId, Product product) {
        var cart = cartRepository.getByKey(cartId)
                .orElseThrow(() -> new IllegalArgumentException("id doesn't correspond to any cart"));
        if (isProductAlreadyInTheCart(cartId, product)) {
            throw new IllegalArgumentException("there is already a product with the same id in the cart");
        }

        var updatedCart = new Cart(cart.getId(),
                Stream.concat(cart.getProducts().stream(), Stream.of(product))
                        .collect(Collectors.toList()));

        cartRepository.update(updatedCart);

        return updatedCart;
    }
}
