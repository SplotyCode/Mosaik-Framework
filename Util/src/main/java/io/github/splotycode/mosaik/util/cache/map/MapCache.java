package io.github.splotycode.mosaik.util.cache.map;

import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

import java.util.function.Function;

/**
 * Cache that can cache multiple values
 */
public interface MapCache<K, V> {

    /**
     * Returns a cached value by its key
     */
    V getValue(K key);

    /**
     * Returns a sup Cache by its key
     * @param create should we create a new sup cache it no cache is found
     */
    Cache<V> getCache(K key, boolean create);

    /**
     * Sets a cache value by its key
     */
    void setValue(K key, V value);

    /**
     * Clears all values
     */
    void clearAll();

    /**
     * Clears a value by a key
     */
    void clear(K key);

    /**
     * Cache Factories are used to construct the sup caches
     */
    void setCacheFactory(Function< K, Cache<V>> cacheFactory);
    Function<K, Cache<V>> getCacheFactory();

    /**
     * Returns the ListenerHandler for this cache
     */
    ListenerHandler getHandler();

}
