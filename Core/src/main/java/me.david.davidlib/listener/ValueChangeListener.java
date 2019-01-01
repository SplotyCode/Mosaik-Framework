package me.david.davidlib.listener;

public interface ValueChangeListener<T> extends Listener {

    void valueChange(T newValue, T oldValue);

}
