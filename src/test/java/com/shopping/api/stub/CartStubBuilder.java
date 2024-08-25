package com.shopping.api.stub;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.shopping.api.model.Cart;

public class CartStubBuilder {

    public static Cart.CartBuilder builder() {
        var random = ThreadLocalRandom.current();
        var amount = random.nextInt(5);
        var products = Stream
                .generate(() -> ProductStubBuilder.builder().build())
                .limit(amount).collect(Collectors.toList());
        return Cart.builder()
                .products(products);
    }
}
