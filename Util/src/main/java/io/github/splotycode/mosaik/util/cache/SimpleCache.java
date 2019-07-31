package io.github.splotycode.mosaik.util.cache;

import io.github.splotycode.mosaik.util.listener.DefaultListenerHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.NoArgsConstructor;

/**
 * Very simple Cache.
 * This Cache has no providers or validations.
 */
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCache<T> implements Cache<T> {

    private final ListenerHandler<CacheListener<T>> handler = new DefaultListenerHandler<>();

    @Getter protected T value;

    @Override
    public void setValue(T value) {
        this.value = value;
        handler.call(o -> o.valueChange(value));
    }

    @Override
    public ListenerHandler<CacheListener<T>> getHandler() {
        return handler;
    }
}
