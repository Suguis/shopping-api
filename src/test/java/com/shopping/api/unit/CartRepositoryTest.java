package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.repository.CartRepository;
import com.shopping.api.stub.CartStubBuilder;

@SpringBootTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void clearDatabase() {
        cartRepository.deleteAll();
    }

    @Test
    void shouldCreate() {
        var cart = CartStubBuilder.builder().build();
        cartRepository.create(cart);
    }

    // TODO: test que el repo crea un ID al cart
    @Test
    void shouldCreateAndGetSame() {
        var cart = cartRepository.create(CartStubBuilder.builder().build());
        var retrieved = cartRepository.getByKey(cart.getId()).orElseThrow();

        assertEquals(cart, retrieved);
    }

    @Test
    void shouldNotFindUnexistingCart() {
        var retrieved = cartRepository.getByKey(UUID.randomUUID());

        assertEquals(Optional.empty(), retrieved);
    }

    @Test
    void shouldReturnDeletedCartsOnDeleteAll() {
        var cartsWithoutId = Set.of(
                CartStubBuilder.builder().build(),
                CartStubBuilder.builder().build());

        var carts = cartsWithoutId.stream()
                .map(cart -> cartRepository.create(cart))
                .collect(Collectors.toSet());

        var retrieved = new HashSet<>(cartRepository.deleteAll().values());

        assertEquals(carts, retrieved);
    }

    @Test
    void shouldNotGetAfterDeletingAll() {
        var cart = CartStubBuilder.builder().build();

        cartRepository.create(cart);

        cartRepository.deleteAll();

        var retrieved = cartRepository.getByKey(cart.getId());

        assertEquals(Optional.empty(), retrieved);
    }
}
