package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class RoundRobin<T> implements Iterable<T> {

    private List<T> list;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private AtomicInteger index = new AtomicInteger();

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public T next() {
                int cIndex = index.getAndUpdate(index -> (index + 1) % list.size());
                return list.get(cIndex);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }
}
