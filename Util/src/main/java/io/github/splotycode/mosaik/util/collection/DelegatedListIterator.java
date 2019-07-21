package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.ListIterator;

@AllArgsConstructor
public class DelegatedListIterator<E> implements ListIterator<E> {

    protected ListIterator<E> delegated;

    @Override
    public boolean hasNext() {
        return delegated.hasNext();
    }

    @Override
    public E next() {
        return delegated.next();
    }

    @Override
    public boolean hasPrevious() {
        return delegated.hasPrevious();
    }

    @Override
    public E previous() {
        return delegated.previous();
    }

    @Override
    public int nextIndex() {
        return delegated.nextIndex();
    }

    @Override
    public int previousIndex() {
        return delegated.previousIndex();
    }

    @Override
    public void remove() {
        delegated.remove();
    }

    @Override
    public void set(E e) {
        delegated.set(e);
    }

    @Override
    public void add(E e) {
        delegated.add(e);
    }
}
