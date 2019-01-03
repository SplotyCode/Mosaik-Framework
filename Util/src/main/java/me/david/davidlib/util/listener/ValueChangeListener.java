package me.david.davidlib.util.listener;

public interface ValueChangeListener<T> extends Listener {

    void valueChange(T newValue, T oldValue);

}
