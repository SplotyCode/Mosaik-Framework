package io.github.splotycode.mosaik.util.collection;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReversedList<E> extends DelegatedList<E> {

    public ReversedList(List<E> delegated) {
        super(delegated);
    }

    private int getRealIndex(int index) {
        return delegated.size() - index - 1;
    }

    @Override
    public void add(int index, E element) {
        super.add(getRealIndex(index + 1), element);
    }

    @Override
    public E remove(int index) {
        return super.remove(getRealIndex(index));
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        subList(fromIndex, toIndex).clear();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new ReversedList<>(super.subList(getRealIndex(fromIndex) + 1, getRealIndex(toIndex) + 1));
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        index = getRealIndex(index) + 1;
        return new DelegatedListIterator<E>(super.listIterator(index)) {
            @Override
            public void add(E e) {
                super.add(e);
                super.previous();
            }

            @Override
            public boolean hasNext() {
                return super.hasPrevious();
            }

            @Override
            public boolean hasPrevious() {
                return super.hasNext();
            }

            @Override
            public E next() {
                return super.previous();
            }

            @Override
            public E previous() {
                return super.next();
            }

            @Override
            public int nextIndex() {
                return getRealIndex(super.nextIndex()) + 1;
            }

            @Override
            public int previousIndex() {
                return nextIndex() - 1;
            }
        };
    }

    @Override
    public E set(int index, E element) {
        return super.set(getRealIndex(index), element);
    }

    @Override
    public E get(int index) {
        return super.get(getRealIndex(index));
    }
}
