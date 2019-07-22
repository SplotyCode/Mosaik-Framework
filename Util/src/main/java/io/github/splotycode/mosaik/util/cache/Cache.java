package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.listener.ListenerHandler;

/**
 * General Single value cache
 */
public interface Cache<T> {

    /**
     * Returns the cashed/generated valid value
     */
    T getValue();

    /**
     * Sets the value that should be cached
     */
    void setValue(T value);

    /**
     * Clears this cache
     */
    default void clear() {
        setValue(null);
    }

    /**
     * Returns the ListenerHandler for this cache
     */
    ListenerHandler<CacheListener<T>> getHandler();

    /**
     * Adds a CacheListener to this ListenerHandler.
     * If the CacheListener is already added nothing will happen
     */
    default void addListener(CacheListener<T> listener) {
        getHandler().addListener(listener);
    }

    /**
     * Removes a CacheListener to this ListenerHandler.
     * If the CacheListener is not there nothing will happen
     */
    default void removeListener(CacheListener<T> listener) {
        getHandler().removeListener(listener);
    }

}
