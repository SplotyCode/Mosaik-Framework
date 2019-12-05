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

            private final AtomicInteger index = new AtomicInteger(-1);

            @Override
            public boolean hasNext() {
                return !list.isEmpty();
            }

            @Override
            public T next() {
                int cIndex = index.updateAndGet(index -> (index + 1) % list.size());
                return list.get(cIndex);
            }

            @Override
            public synchronized void remove() {
                int currentIndex = index.get();
                if (currentIndex == -1) {
                    currentIndex = 0;
                }
                list.remove(currentIndex);
            }

        };
    }
}
