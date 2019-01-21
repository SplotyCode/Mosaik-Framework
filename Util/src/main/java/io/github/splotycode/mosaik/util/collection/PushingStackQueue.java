package io.github.splotycode.mosaik.util.collection;

import io.github.splotycode.mosaik.util.exception.MethodNotSupportedExcpetion;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PushingStackQueue<E> extends ArrayBlockingQueue<E> {

    private Supplier<E> supplier;
    private int cap;

    @Override
    public E peek() {
        if (size() < cap) {
            E item = supplier.get();
            if (item != null) super.offer(item);
        }
        E item = null;
        while (item == null) {
            item = super.peek();
            if (item == null) {
                item = supplier.get();
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public boolean offer(E e) {
        boolean result = super.offer(e);
        notify();
        return result;
    }

    @Override
    public E element() {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean add(E e) {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean offer(E e, long l, TimeUnit timeUnit) throws InterruptedException {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public E remove() {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean removeIf(Predicate<? super E> predicate) {
        throw new MethodNotSupportedExcpetion();
    }

    @Override
    public boolean remove(Object o) {
        throw new MethodNotSupportedExcpetion();
    }

    public PushingStackQueue(int cap, Supplier<E> supplier) {
        super(cap);
        this.supplier = supplier;
        this.cap = cap;
    }

    public PushingStackQueue(int cap, boolean b, Supplier<E> supplier) {
        super(cap, b);
        this.supplier = supplier;
        this.cap = cap;
    }

    public PushingStackQueue(int cap, boolean b, Collection<? extends E> collection, Supplier<E> supplier) {
        super(cap, b, collection);
        this.supplier = supplier;
        this.cap = cap;
    }
}
