package ru.otus.cachehw;


import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cache = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, CacheOperation.PUT);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, null, CacheOperation.REMOVE);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
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
}
