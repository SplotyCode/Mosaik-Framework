package me.david.davidlib.cache;

import me.david.davidlib.listener.Listener;

public interface CacheListener<T> extends Listener {

    void valueChange(T value);

}
