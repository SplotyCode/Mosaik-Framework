package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.listener.Listener;

/**
 * Listener to catch value changes in a cache
 */
public interface CacheListener<T> extends Listener {

    void valueChange(T value);

}
