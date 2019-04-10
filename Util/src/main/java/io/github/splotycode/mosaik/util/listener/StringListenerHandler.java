package io.github.splotycode.mosaik.util.listener;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class StringListenerHandler<L extends Listener> implements ListenerHandler<L> {

    private Multimap<String, L> listeners = HashMultimap.create();

    public void addListener(String prefix, L listener) {
        listeners.put(prefix, listener);
    }

    public void addListener(Class clazz, L listener) {
        listeners.put(clazz.getName(), listener);
    }

    @Override
    public void addListener(L listener) {
        addListener("*", listener);
    }

    @Override
    public Collection<L> getListeners() {
        return listeners.values();
    }

    public void call(String prefix, Consumer<L> consumer) {
        for (Map.Entry<String, L> listener : listeners.entries()) {
            if (listener.getKey().equals("*") || listener.getKey().startsWith(prefix)) {
                consumer.accept(listener.getValue());
            }
        }
    }

    @Override
    public void call(Consumer<L> consumer) {
        getListeners().forEach(consumer);
    }
}
