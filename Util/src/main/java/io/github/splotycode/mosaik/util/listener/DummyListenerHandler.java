package io.github.splotycode.mosaik.util.listener;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class DummyListenerHandler<L extends Listener> implements ListenerHandler<L> {

    public static DummyListenerHandler DUMMY = new DummyListenerHandler();

    public static <L extends Listener> DummyListenerHandler<L> dummy() {
        return DUMMY;
    }

    @Override
    public Collection<L> getListeners() {
        return Collections.emptyList();
    }

    @Override public void call(Consumer<L> consumer) {}
    @Override public void removeListener(L listener) {}
    @Override public void addListener(L listener) {}

}
