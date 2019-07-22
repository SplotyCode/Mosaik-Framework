package io.github.splotycode.mosaik.util.listener;

import io.github.splotycode.mosaik.util.condition.ClassConditions;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultipleListenerHandler<L extends Listener> implements ListenerHandler<L> {

    @Getter private List<L> listeners = new ArrayList<>();

    public <I extends L> void call(Class<? extends I> clazz, Consumer<I> consumer){
        listeners.stream().filter(ClassConditions.assignableClass(clazz)).forEach(l -> consumer.accept((I) l));
    }

    public void call(Consumer<L> consumer){
        listeners.forEach(consumer);
    }
}
