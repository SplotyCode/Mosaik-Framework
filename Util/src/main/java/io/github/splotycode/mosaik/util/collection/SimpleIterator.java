package io.github.splotycode.mosaik.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SimpleIterator<T> implements Iterator<T> {

    protected T next;
    protected boolean hasElement;

    protected abstract boolean provideNext();

    @Override
    public boolean hasNext() {
        return hasElement || (hasElement = provideNext());
    }

    public T peekNext() {
        if (hasNext()) {
            return next;
        }
        throw new NoSuchElementException();
    }

    @Override
    public T next() {
        try {
            return peekNext();
        } finally {
            hasElement = false;
        }
    }

}
