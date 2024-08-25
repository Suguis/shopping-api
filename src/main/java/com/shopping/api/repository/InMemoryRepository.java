package com.shopping.api.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRepository<K, E> {

    Map<K, E> elements = new ConcurrentHashMap<>();

    public void put(K key, E element) {
        elements.put(key, element);
    }

    public Optional<E> getByKey(K key) {
        return Optional.ofNullable(elements.get(key));
    }

    public Optional<E> deleteByKey(K key) {
        return Optional.ofNullable(elements.remove(key));
    }

    public Map<K, E> deleteAll() {
        var deleted = elements;
        elements = new HashMap<>();
        return deleted;
    };
}
