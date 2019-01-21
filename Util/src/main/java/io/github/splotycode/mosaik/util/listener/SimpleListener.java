package io.github.splotycode.mosaik.util.listener;

public interface SimpleListener<T> extends Listener {

    void event(T event);

}
