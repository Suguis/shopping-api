package com.shopping.api.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.api.model.Cart;
import com.shopping.api.repository.CartRepository;
import com.shopping.api.stub.CartStubBuilder;
import com.shopping.api.stub.ProductStubBuilder;

// TODO: add test for update

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

    @Test
    void shouldAddAnIdWhenCartCreatedInRepository() {
        var cart = CartStubBuilder.builder().build();
        var cartWithId = cartRepository.create(cart);

        assertEquals(cart.getProducts(), cartWithId.getProducts());
        assertTrue(cart.getId().isEmpty());
        assertTrue(cartWithId.getId().isPresent());
    }

    @Test
    void shouldCreateAndGetSame() {
        var cart = cartRepository.create(CartStubBuilder.builder().build());
        var retrieved = cartRepository.getByKey(cart.getId().get()).orElseThrow();

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
        var cart = cartRepository.create(CartStubBuilder.builder().build());

        cartRepository.deleteAll();

        var retrieved = cartRepository.getByKey(cart.getId().get());

        assertEquals(Optional.empty(), retrieved);
    }

    @Test
    void shouldDelete() {
        var cart = cartRepository.create(CartStubBuilder.builder().build());

        var deletedCart = cartRepository.deleteByKey(cart.getId().get()).orElseThrow();

        assertEquals(cart, deletedCart);
        assertTrue(cartRepository.getByKey(cart.getId().get()).isEmpty());
    }

    @Test
    void shouldUpdate() {
        var cart = cartRepository.create(CartStubBuilder.builder().build());
        var product = ProductStubBuilder.builder().build();
        var updatedCart = new Cart(cart.getId().get(), Stream.concat(cart.getProducts().stream(), Stream.of(product))
                .collect(Collectors.toList()));

        cartRepository.update(updatedCart);

        var retrieved = cartRepository.getByKey(cart.getId().get()).get();
        assertEquals(retrieved, updatedCart);
    }

    @Test
    void shouldThrowWhenUpdatingCartWithoutId() {
        var cart = CartStubBuilder.builder().build();

        assertThrows(NoSuchElementException.class, () -> cartRepository.update(cart));
    }
}
