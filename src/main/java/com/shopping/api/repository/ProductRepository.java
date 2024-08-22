package com.shopping.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.shopping.api.model.Product;

@Repository
public class ProductRepository {

    List<Product> products = new ArrayList<>();

    public Product create(Product product) {
        products.add(product);
        return product;
    }

    public Optional<Product> getById(Long id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    public List<Product> deleteAll() {
        var deleted = products;
        products = new ArrayList<>();
        return deleted;
    }
}
