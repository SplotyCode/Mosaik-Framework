package io.github.splotycode.mosaik.util.listener;

import io.github.splotycode.mosaik.util.condition.ClassConditions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultipleListenerHandler<L extends Listener> implements ListenerHandler<L> {

    @Getter private List<L> listeners = new ArrayList<>();

    public void call(Class<? extends L> clazz, Consumer<L> consumer){
        listeners.stream().filter(ClassConditions.assignableClass(clazz)).forEach(consumer);
    }

    public void call(Consumer<L> consumer){
        listeners.forEach(consumer);
    }
}
