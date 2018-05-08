package me.david.davidlib.listener.listener;

public interface SimpleListener<T> extends Listener {

    void event(T event);

}
