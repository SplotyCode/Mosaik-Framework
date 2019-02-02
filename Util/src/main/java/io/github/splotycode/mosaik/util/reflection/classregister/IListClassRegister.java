package io.github.splotycode.mosaik.util.reflection.classregister;

import java.util.ArrayList;
import java.util.Collection;

public interface IListClassRegister<T> extends ClassRegister<T> {

    Collection<T> getList();

    @Override
    default Collection<T> getAll() {
        return getList();
    }

    @Override
    default void register(T obj) {
        getList().add(obj);
    }

    @Override
    default void unRegister(T obj) {
        getList().remove(obj);
    }

    default ArrayList<T> combind(IListClassRegister<T> register) {
        return combind(register.getList());
    }

    default ArrayList<T> combind(Collection<T> collection) {
        ArrayList<T> list = new ArrayList<>(collection);
        list.addAll(getList());
        return list;
    }

}
