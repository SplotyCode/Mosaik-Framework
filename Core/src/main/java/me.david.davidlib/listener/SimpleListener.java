package me.david.davidlib.listener;

public interface SimpleListener<T> extends Listener {

    void event(T event);

}
