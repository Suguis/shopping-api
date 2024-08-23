package com.shopping.api.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.api.model.Cart;
import com.shopping.api.service.CartService;

// TODO: Add /api as basepath

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Cart createCart() {
        var cart = cartService.create();
        return cart;
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Optional<Cart> createCart(@PathVariable UUID id) {
        return cartService.get(id);
    }

}
