package com.shopping.api.integration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.repository.CartRepository;
import com.shopping.api.service.CartService;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private Duration cartDeletionTime;

    @BeforeEach
    void clearDatabase() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldDeleteCartsAfterDeletionTime() throws InterruptedException {
        var cart1 = cartService.create();
        var cart2 = cartService.create();

        cartService.scheduleDeletion(cart1.getId().get());
        cartService.scheduleDeletion(cart2.getId().get());
        assertTrue(cartService.get(cart1.getId().get()).isPresent());
        assertTrue(cartService.get(cart2.getId().get()).isPresent());

        var awaitTime = cartDeletionTime.multipliedBy(2);
        await().atMost(awaitTime).untilAsserted(() -> {
            assertTrue(cartService.get(cart1.getId().get()).isEmpty());
            assertTrue(cartService.get(cart2.getId().get()).isEmpty());
        });
    }

    @Test
    void shouldNotDeleteCartIfReeschedulde() throws InterruptedException {
        var cart1 = cartService.create();
        var cart2 = cartService.create();

        cartService.scheduleDeletion(cart1.getId().get());
        cartService.scheduleDeletion(cart2.getId().get());
        assertTrue(cartService.get(cart1.getId().get()).isPresent());
        assertTrue(cartService.get(cart2.getId().get()).isPresent());

        var awaitTime = cartDeletionTime.dividedBy(2);

        for (int i = 0; i < 5; i++) {
            Thread.sleep(awaitTime.toMillis());
            cartService.scheduleDeletion(cart1.getId().get());
            cartService.scheduleDeletion(cart2.getId().get());
        }

        assertTrue(cartService.get(cart1.getId().get()).isPresent());
        assertTrue(cartService.get(cart2.getId().get()).isPresent());
    }

}
