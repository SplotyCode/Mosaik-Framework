package io.github.splotycode.mosaik.util.cache.map;

import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

import java.util.function.Function;

public interface MapCache<K, V> {

    V getValue(K key);

    Cache<V> getCache(K key, boolean create);

    void setValue(K key, V value);

    void clearAll();

    void clear(K key);

    void setCacheFactory(Function< K, Cache<V>> cacheFactory);
    Function<K, Cache<V>> getCacheFactory();

    ListenerHandler getHandler();

}
