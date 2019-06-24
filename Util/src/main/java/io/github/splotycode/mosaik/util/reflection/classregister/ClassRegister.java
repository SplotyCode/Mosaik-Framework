package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;

import java.util.ArrayList;
import java.util.Collection;

public interface ClassRegister<T> {

    Logger logger = Logger.getInstance(ClassRegister.class);

    void register(T obj);
    void unRegister(T obj);

    Collection<T> getAll();

    default void register(Class<? extends T> clazz) {
        try {
            register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(clazz.getName() + " has not zero argument constructor", e);
        }
    }

    default void unRegister(Class<? extends T> clazz) {
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().equals(clazz)) {
                unRegister(obj);
            }
        }
    }

    default void register(String clazz) {
        try {
            register((T) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            logger.warn("Class " + clazz + " is not found by class loader");
        }
    }

    default void unRegister(String clazz) {
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().getSimpleName().equals(clazz)) {
                unRegister(obj);
            }
        }
    }

    Class<T> getObjectClass();

    default void registerAll(ClassCollector collector) {
        for (Class clazz : collector.collectAll()) {
            if (getObjectClass().isAssignableFrom(clazz)) {
                register(clazz);
            } else {
                logger.warn(clazz.getName() + " can not be registered because collector type is: " + getObjectClass().getName());
            }
        }
    }

    default void registerPackage(String path) {
        for (Class clazz : ClassFinderHelper.getUserClasses()) {
            if (clazz.getName().startsWith(path) && getObjectClass().isAssignableFrom(clazz)) {
                register((Class<? extends T>) clazz);
            }
        }
    }

    default void unRegisterPackage(String path) {
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().getSimpleName().startsWith(path)) {
                unRegister(obj);
            }
        }
    }

}
