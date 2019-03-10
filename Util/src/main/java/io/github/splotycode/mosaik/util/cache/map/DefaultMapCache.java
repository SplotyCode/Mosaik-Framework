package io.github.splotycode.mosaik.util.cache.map;

import io.github.splotycode.mosaik.util.Destroyable;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class DefaultMapCache<K, V> implements MapCache<K, V>, CacheListener<V> {

    protected Map<K, Cache<V>> caches = new HashMap<>();
    protected ListenerHandler<CacheListener<V>> globalHandler = new ListenerHandler<>();
    @Getter @Setter protected Function<K, Cache<V>> cacheFactory;

    @Override
    public V getValue(K key) {
        Cache<V> cache = getCache(key, false);
        if (cache != null) {
            return cache.getValue();
        }
        return null;
    }

    @Override
    public Cache<V> getCache(K key, boolean create) {
        Cache<V> cache = caches.get(key);
        if (cache == null && create) {
            cache = cacheFactory.apply(key);
            caches.put(key, cache);
            cache.addListener(this);
        }
        return cache;
    }

    @Override
    public void setValue(K key, V value) {
        if (value == null) {
            clear(key);
        } else {
            getCache(key, true).setValue(value);
        }
    }

    @Override
    public void clearAll() {
        caches.clear();
    }

    @Override
    public void clear(K key) {
        Cache<V> cache = getCache(key, false);
        if (cache != null) {
            if (cache.getValue() != null && cache.getValue() instanceof Destroyable) {
                ((Destroyable) cache.getValue()).destroy();
            }
            cache.clear();
        }
    }

    @Override
    public ListenerHandler getHandler() {
        return globalHandler;
    }

    @Override
    public void valueChange(V value) {
        globalHandler.call(listener -> listener.valueChange(value));
    }
}
