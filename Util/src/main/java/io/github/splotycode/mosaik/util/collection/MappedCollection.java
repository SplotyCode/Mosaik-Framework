package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
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

            @Override
            public void remove() {
                delegated.remove();
            }

        };
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return original.removeIf(o -> filter.test(mapper.apply(o)));
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
