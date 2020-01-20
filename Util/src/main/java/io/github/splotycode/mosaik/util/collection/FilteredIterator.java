package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Predicate;

@AllArgsConstructor
public class FilteredIterator<E> extends SimpleIterator<E> {

    private Iterator<E> wrapped;
    private Predicate<E> filter;

    @Override
    protected boolean provideNext() {
        while (wrapped.hasNext()) {
            E element = wrapped.next();
            if (filter.test(element)) {
                next = element;
                return true;
            }
        }
        return false;
    }
}
