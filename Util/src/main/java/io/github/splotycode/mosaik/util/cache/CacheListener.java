package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface CacheListener<T> extends Listener {

    void valueChange(T value);

}
