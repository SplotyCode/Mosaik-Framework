package io.github.splotycode.mosaik.util.listener;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DefaultListenerHandler<L extends Listener> implements ListenerHandler<L> {

    @Getter private List<L> listeners;

    public DefaultListenerHandler(){
        listeners = new ArrayList<>();
    }

    public DefaultListenerHandler(List<L> listeners) {
        this.listeners = listeners;
    }

    @SafeVarargs
    public DefaultListenerHandler(L... listeners){
        this();
        this.listeners.addAll(new ArrayList<>(Arrays.asList(listeners)));
    }

    public void call(Consumer<L> consumer){
        listeners.forEach(consumer);
    }

}
