package com.shopping.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Cart {

    private UUID id;

    @NonNull
    @Default
    List<Product> products = new ArrayList<>();

    public Cart(UUID id, Cart cart) {
        this(id, List.copyOf(cart.products));
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Cart addProduct(Product product) {
        var newProducts = new ArrayList<>(products);
        newProducts.add(product);
        return new Cart(id, newProducts);
    }

    public Optional<UUID> getId() {
        return Optional.ofNullable(id);
    }
}
