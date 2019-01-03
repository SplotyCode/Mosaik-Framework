package me.david.davidlib.util.cache;

import me.david.davidlib.util.listener.Listener;

public interface CacheListener<T> extends Listener {

    void valueChange(T value);

}
