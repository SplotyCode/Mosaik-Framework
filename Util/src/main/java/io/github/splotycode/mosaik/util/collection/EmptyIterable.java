package io.github.splotycode.mosaik.util.collection;

import java.util.*;
import java.util.function.Consumer;

public class EmptyIterable<T> implements Iterable<T> {

    private static final EmptyIterable EMPTY_ITERABLE = new EmptyIterable();

    public static <T> EmptyIterable<T> emptyIterable() {
        return EMPTY_ITERABLE;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.emptySpliterator();
    }
}
