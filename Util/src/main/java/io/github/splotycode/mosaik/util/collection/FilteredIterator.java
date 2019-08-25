package io.github.splotycode.mosaik.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class FilteredIterator<E> implements Iterator<E> {

    private Iterator<E> wrapped;
    private Predicate<E> filter;
    private E next;

    public FilteredIterator(Iterator<E> wrapped, Predicate<E> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
        getNext();
    }

    private E getNext() {
        E current = next;
        while (wrapped.hasNext()) {
            if (filter.test(next = wrapped.next())) {
                return current;
            }
        }
        return (next = null);
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public E next() {
        E next = getNext();
        if (next == null) {
            throw new NoSuchElementException();
        }
        return next;
    }
}
