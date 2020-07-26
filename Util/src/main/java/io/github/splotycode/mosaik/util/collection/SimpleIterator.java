package io.github.splotycode.mosaik.util.collection;

import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

@NoArgsConstructor
public abstract class SimpleIterator<T> implements Iterator<T> {

    @NoArgsConstructor
    public static abstract class NotNullSimpleIterator<T> extends SimpleIterator<T> {

        public NotNullSimpleIterator(T next) {
            super(next);
        }

        protected abstract T provideNext0();

        @Override
        protected boolean provideNext() {
            next = provideNext0();
            return next != null;
        }
    }

    protected T next;
    protected boolean hasElement;

    public SimpleIterator(T next) {
        this.next = next;
        this.hasElement = true;
    }

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
