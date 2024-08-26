package com.shopping.api.stub;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

import com.shopping.api.model.Product;

public class ProductStubBuilder {

    public static Product.ProductBuilder builder() {
        var random = ThreadLocalRandom.current();
        var id = random.nextLong(Long.MAX_VALUE - 1) + 1;
        return Product.builder()
                .id(id)
                .description(RandomStringUtils.randomAlphabetic(0, 100))
                .amount(random.nextInt(9) + 1);
    }
}
