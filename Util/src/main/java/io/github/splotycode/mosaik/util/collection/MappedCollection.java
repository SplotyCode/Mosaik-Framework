package io.github.splotycode.mosaik.util.collection;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class MappedCollection<E, O> extends AbstractCollection<E> {

    private Collection<O> original;
    private Function<O, E> mapper;

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private Iterator<O> delegated = original.iterator();

            @Override
            public boolean hasNext() {
                return delegated.hasNext();
            }

            @Override
            public E next() {
                return mapper.apply(delegated.next());
            }
        };
    }

    @Override
    public void clear() {
        original.clear();
    }

    @Override
    public int size() {
        return original.size();
    }
}
