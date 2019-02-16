package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public interface MapCache<K, V> {

    V getValue(K key);

    void setValue(K key, V value);

    void clearAll();

    void clear(K key);

    ListenerHandler getHandler();



}
