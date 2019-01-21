package io.github.splotycode.mosaik.util.cache;

import lombok.Getter;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;

public class SimpleCache<T> implements Cache<T> {

    private ListenerHandler<CacheListener<T>> handler = new ListenerHandler<>();

    @Getter private T value;

    @Override
    public void setValue(T value) {
        this.value = value;
        handler.call(o -> o.valueChange(value));
    }

    @Override
    public ListenerHandler getHandler() {
        return handler;
    }
}
