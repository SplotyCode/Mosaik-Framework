package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;

public interface ClassRegister<T> extends RawClassRegister<Class<? extends T>> {

    Logger LOGGER = Logger.getInstance(ClassRegister.class);

    void register(T obj);
    void unRegister(T obj);

    Collection<T> getAll();

    @Override
    default void register(Class<? extends T> clazz) {
        try {
            register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(clazz.getName() + " has not zero argument constructor", e);
        }
    }

    @Override
    default void unRegister(Class<? extends T> clazz) {
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().equals(clazz)) {
                unRegister(obj);
            }
        }
    }

    @Override
    default void unRegisterPackage(String path) {
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().getSimpleName().startsWith(path)) {
                unRegister(obj);
            }
        }
    }

}
