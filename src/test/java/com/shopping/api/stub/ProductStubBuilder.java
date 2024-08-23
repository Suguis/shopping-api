package com.shopping.api.stub;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.shopping.api.model.Product;

public class ProductStubBuilder {

    private static Random random = new Random();

    public static Product.ProductBuilder builder() {
        return Product.builder()
                .id(random.nextLong(Long.MAX_VALUE - 1) + 1)
                .description(RandomStringUtils.randomAlphabetic(0, 100))
                .amount(random.nextInt(10));
    }
}
