package me.david.davidlib.cache;

import me.david.davidlib.listener.ListenerHandler;

public interface Cache<T> {

    T getValue();
    void setValue(T value);

    default void clear() {
        setValue(null);
    }

    ListenerHandler getHandler();

    default void addListener(CacheListener<T> listener) {
        getHandler().addListener(listener);
    }

    default void removeListener(CacheListener<T> listener) {
        getHandler().removeListener(listener);
    }

}
