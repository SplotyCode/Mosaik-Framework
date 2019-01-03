package me.david.davidlib.util.listener;

public interface SimpleListener<T> extends Listener {

    void event(T event);

}
