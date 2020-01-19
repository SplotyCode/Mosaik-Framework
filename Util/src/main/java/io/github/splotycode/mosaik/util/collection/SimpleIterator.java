package io.github.splotycode.mosaik.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SimpleIterator<T> implements Iterator<T> {

    protected T next;

    protected abstract boolean provideNext();


    @Override
    public boolean hasNext() {
        return next != null || provideNext();
    }

    public T peekNext() {
        if (next != null || provideNext()) {
            return next;
        }
        throw new NoSuchElementException();
    }

    @Override
    public T next() {
        try {
            return peekNext();
        } finally {
            next = null;
        }
    }

}
