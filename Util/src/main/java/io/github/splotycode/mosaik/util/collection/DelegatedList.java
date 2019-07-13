package io.github.splotycode.mosaik.util.collection;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@AllArgsConstructor
public class DelegatedList<E> extends AbstractList<E> {
    
    protected List<E> delegated;

    @Override
    public boolean add(E e) {
        return delegated.add(e);
    }

    @Override
    public E set(int index, E element) {
        return delegated.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        delegated.add(index, element);
    }

    @Override
    public E remove(int index) {
        return delegated.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegated.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegated.lastIndexOf(o);
    }

    @Override
    public void clear() {
        delegated.clear();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return delegated.addAll(index, c);
    }

    @Override
    public Iterator<E> iterator() {
        return delegated.iterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return delegated.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegated.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegated.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        return delegated.equals(o);
    }

    @Override
    public int hashCode() {
        return delegated.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return delegated.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegated.contains(o);
    }

    @Override
    public Object[] toArray() {
        return delegated.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegated.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return delegated.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegated.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return delegated.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegated.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegated.retainAll(c);
    }

    @Override
    public String toString() {
        return delegated.toString();
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        delegated.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        delegated.sort(c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegated.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return delegated.removeIf(filter);
    }

    @Override
    public Stream<E> stream() {
        return delegated.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return delegated.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        delegated.forEach(action);
    }

    @Override
    public E get(int index) {
        return delegated.get(index);
    }

    @Override
    public int size() {
        return delegated.size();
    }
}
