package me.david.davidlib.util.cache;

import me.david.davidlib.util.listener.ListenerHandler;

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
