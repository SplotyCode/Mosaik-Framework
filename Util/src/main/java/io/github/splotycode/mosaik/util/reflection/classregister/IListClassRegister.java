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

    default Collection<T> combind(IListClassRegister<T> register) {
        if (register == null) return getList();
        return combind(register.getList());
    }

    default Collection<T> combind(Collection<T> collection) {
        if (collection == null) return getList();
        ArrayList<T> list = new ArrayList<>(collection);
        list.addAll(getList());
        return list;
    }

}
