package me.david.davidlib.utils.reflection.classregister;

import java.util.Collection;

public interface IListClassRegister<T> extends ClassRegister<T> {

    Collection<T> getList();

    @Override
    default void register(T obj) {
        getList().add(obj);
    }

    @Override
    default void unRegister(T obj) {
        getList().remove(obj);
    }

}
