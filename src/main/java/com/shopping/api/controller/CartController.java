package com.shopping.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.api.model.Cart;
import com.shopping.api.service.CartService;

@RestController
@RequestMapping("/carts")
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
    public ResponseEntity<Cart> createCart(@PathVariable UUID id) {
        return cartService.get(id).map(cart -> ResponseEntity.ok(cart))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
