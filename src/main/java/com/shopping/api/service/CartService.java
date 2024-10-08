package com.shopping.api.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.api.config.AppConfig;
import com.shopping.api.model.Cart;
import com.shopping.api.model.Product;
import com.shopping.api.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AppConfig appConfig;

    private ScheduledExecutorService cartDeletionExecutor = new ScheduledThreadPoolExecutor(1);

    private Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public Cart create() {
        var cart = cartRepository.create(Cart.builder().build());
        scheduleDeletion(cart.getId().get());
        return cart;
    }

    public Optional<Cart> get(UUID id) {
        scheduleDeletion(id);
        return cartRepository.getByKey(id);
    }

    public Optional<Cart> delete(UUID id) {
        var scheduledTask = scheduledTasks.remove(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        return cartRepository.deleteByKey(id);
    }

    public boolean isProductAlreadyInTheCart(UUID id, Product product) {
        return get(id).map(cart -> cart.getProducts().stream().anyMatch(p -> p.getId() == product.getId()))
                .orElseThrow(() -> new IllegalArgumentException("id doesn't correspond to any cart"));
    }

    public synchronized Cart addProduct(UUID cartId, Product product) {
        var cart = cartRepository.getByKey(cartId)
                .orElseThrow(() -> new IllegalArgumentException("id doesn't correspond to any cart"));
        if (isProductAlreadyInTheCart(cartId, product)) {
            throw new IllegalArgumentException("there is already a product with the same id in the cart");
        }

        var updatedCart = cart.addProduct(product);

        cartRepository.update(updatedCart);

        scheduleDeletion(cartId);

        return updatedCart;
    }

    public void scheduleDeletion(UUID id) {
        var oldTask = scheduledTasks.get(id);
        if (oldTask != null) {
            oldTask.cancel(false);
        }
        scheduledTasks.put(id,
                cartDeletionExecutor.schedule(() -> cartRepository.deleteByKey(id),
                        appConfig.getCartDeletionTime().toMillis(),
                        TimeUnit.MILLISECONDS));
    }
}
