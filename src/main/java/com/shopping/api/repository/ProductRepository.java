package com.shopping.api.repository;

import org.springframework.stereotype.Repository;

import com.shopping.api.model.Product;

@Repository
public class ProductRepository extends InMemoryRepository<Long, Product> {
    public void create(Product product) {
        create(product.getId(), product);
    }
}
