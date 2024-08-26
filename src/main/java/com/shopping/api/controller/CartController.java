package com.shopping.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.api.model.Cart;
import com.shopping.api.model.Product;
import com.shopping.api.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates an empty cart, with no products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))
            })
    })
    public Cart createCart() {
        var cart = cartService.create();
        return cart;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a cart given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))
            }),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
    })
    public ResponseEntity<Cart> getCart(@PathVariable UUID id) {
        return cartService.get(id).map(cart -> ResponseEntity.ok(cart))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a cart given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
    })
    public ResponseEntity<Object> deleteCart(@PathVariable UUID id) {
        return cartService.delete(id).map(cart -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{id}/products")
    @Operation(summary = "Adds a product to a cart given by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The product was successfully created in the cart", content = @Content),
            @ApiResponse(responseCode = "400", description = "The product is malformed", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "A product with the same id already exists in the cart", content = @Content),
    })
    public ResponseEntity<Object> addProduct(@PathVariable UUID id, @Valid @RequestBody Product product) {
        return cartService.get(id).map(cart -> {
            if (cartService.isProductAlreadyInTheCart(id, product)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            cartService.addProduct(id, product);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
