package io.github.splotycode.mosaik.util.listener;

import java.util.List;
import java.util.function.Consumer;

public interface ListenerHandler<L extends Listener> {

    List<L> getListeners();
    void call(Consumer<L> consumer);

    default void addListener(L listener) {
        if(!getListeners().contains(listener))
            getListeners().add(listener);
    }

    default void removeListener(L listener) {
        getListeners().remove(listener);
    }

}
