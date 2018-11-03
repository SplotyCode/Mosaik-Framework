package me.david.davidlib.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ListenerHandler<L extends Listener> {

    private List<L> listeners;

    public ListenerHandler(){
        listeners = new ArrayList<>();
    }

    public ListenerHandler(List<L> listeners) {
        this.listeners = listeners;
    }

    @SafeVarargs
    public ListenerHandler(L... listeners){
        this();
        this.listeners.addAll(new ArrayList<>(Arrays.asList(listeners)));
    }

    public void addListener(L listener){
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(L listener){
        if(listeners.contains(listener))
            listeners.remove(listener);
    }

    public void call(Consumer<L> consumer){
        listeners.forEach(consumer);
    }

    public List<L> getListeners() {
        return listeners;
    }
}
