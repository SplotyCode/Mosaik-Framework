package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.function.Predicate;

@AllArgsConstructor
public class FilteredCollection<E> extends AbstractCollection<E> {

    private Iterable<E> wrapped;
    private Predicate<E> filter;

    @Override
    public Iterator<E> iterator() {
        return new FilteredIterator<>(wrapped.iterator(), filter);
    }

    @Override
    public boolean isEmpty() {
        for (E element : wrapped) {
            if (filter.test(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        int size = 0;
        for (E element : wrapped) {
            if (filter.test(element)) {
                size++;
            }
        }
        return size;
    }
}
