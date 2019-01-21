package io.github.splotycode.mosaik.util.listener;

public interface ValueChangeListener<T> extends Listener {

    void valueChange(T newValue, T oldValue);

}
