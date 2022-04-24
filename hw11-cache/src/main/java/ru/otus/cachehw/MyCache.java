package ru.otus.cachehw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        logger.info("Cache size: {}", cache.size());
        notifyListeners(key, value, CacheOperation.PUT);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        logger.info("Cache size: {}", cache.size());
        notifyListeners(key, null, CacheOperation.REMOVE);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        logger.info("Cache size: {}", cache.size());
        notifyListeners(key, value, CacheOperation.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, HwCache.CacheOperation operation) {
        listeners.forEach(listener -> listener.notify(key, value, operation.toString()));
    }

    public void destroy() {
        this.cache.clear();
        this.listeners.clear();
    }
}
