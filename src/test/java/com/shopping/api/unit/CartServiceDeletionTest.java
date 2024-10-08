package com.shopping.api.unit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.config.AppConfig;
import com.shopping.api.repository.CartRepository;
import com.shopping.api.service.CartService;

@SpringBootTest(properties = "cart.deletion.time=300")
public class CartServiceDeletionTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AppConfig appConfig;

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

        var awaitTime = appConfig.getCartDeletionTime().multipliedBy(4);
        await().atMost(awaitTime).untilAsserted(() -> {
            assertTrue(cartRepository.getByKey(cart1.getId().get()).isEmpty());
            assertTrue(cartRepository.getByKey(cart2.getId().get()).isEmpty());
        });
    }

    @Test
    void shouldNotDeleteCartsIfReescheduled() throws InterruptedException {
        var cart1 = cartService.create();
        var cart2 = cartService.create();

        cartService.scheduleDeletion(cart1.getId().get());
        cartService.scheduleDeletion(cart2.getId().get());
        assertTrue(cartService.get(cart1.getId().get()).isPresent());
        assertTrue(cartService.get(cart2.getId().get()).isPresent());

        var pollingTime = appConfig.getCartDeletionTime().dividedBy(2);
        var awaitTime = appConfig.getCartDeletionTime().multipliedBy(3);
        await().during(awaitTime).atMost(awaitTime.plusSeconds(2))
                .pollInterval(pollingTime)
                .untilAsserted(() -> {
                    assertTrue(cartService.get(cart1.getId().get()).isPresent());
                    assertTrue(cartService.get(cart2.getId().get()).isPresent());
                });

        assertTrue(cartService.get(cart1.getId().get()).isPresent());
        assertTrue(cartService.get(cart2.getId().get()).isPresent());
    }
}
