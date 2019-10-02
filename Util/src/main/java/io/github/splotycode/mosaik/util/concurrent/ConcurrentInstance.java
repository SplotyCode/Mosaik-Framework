package io.github.splotycode.mosaik.util.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConcurrentInstance<T> {

    private ArrayBlockingQueue<T> queue;

    public ConcurrentInstance(int instances, Supplier<T> generator) {
        queue = new ArrayBlockingQueue<>(instances);
        for (int i = 0; i < instances; i++) {
            queue.offer(generator.get());
        }
    }

    public T acquireInstance() {
        return acquireInstance(-1);
    }

    public T acquireInstance(int timeout) {
        try {
            return timeout == -1 ? queue.take() : queue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void releaseInstance(T instance) {
        if (!queue.offer(instance)) {
            throw new IllegalStateException("Tried to release a instance but queue is full");
        }
    }

    public boolean useInstance(Consumer<T> callback) {
        return useInstance(callback, -1);
    }

    public boolean useInstance(Consumer<T> callback, int timeout) {
        T instance = null;
        try {
            instance = acquireInstance(timeout);
            if (instance == null) {
                return false;
            }
            callback.accept(instance);
            return true;
        } finally {
            if (instance != null) {
                releaseInstance(instance);
            }
        }
    }



}
