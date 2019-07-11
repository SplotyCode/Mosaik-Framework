package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import io.github.splotycode.mosaik.util.reflection.GenericGuesser;

import java.util.HashMap;
import java.util.Map;

public interface RawClassRegister<T extends Class> {

    Logger LOGGER = Logger.getInstance(RawClassRegister.class);
    Map<Class, Class> TYPE_STORE = new HashMap<>();

    void register(T clazz);
    void unRegister(T clazz);

    @SuppressWarnings("unchecked")
    default T getObjectClass() {
        return (T) TYPE_STORE.computeIfAbsent(getClass(), dummy -> {
           Class clazz = GenericGuesser.find(getClass(), RawClassRegister.class, "T");
           if (clazz == null) {
               throw new IllegalStateException("GenericGuesser could not find generic please override getObjectClass()");
           }
           return clazz;
        });
    }

    default void register(String clazz) {
        try {
            register((T) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Class " + clazz + " is not found by class loader");
        }
    }

    default void unRegister(String clazz) {
        try {
            unRegister((T) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Class " + clazz + " is not found by class loader");
        }
    }

    default void registerPackage(String path) {
        for (Class clazz : ClassFinderHelper.getUserClasses()) {
            if (clazz.getName().startsWith(path) && getObjectClass().isAssignableFrom(clazz)) {
                register((T) clazz);
            }
        }
    }

    default void registerPackage(T packageClass) {
        registerPackage(packageClass.getPackage().getName());
    }

    default void unRegisterPackage(String path) {
        for (Class clazz : ClassFinderHelper.getUserClasses()) {
            if (clazz.getName().startsWith(path) && getObjectClass().isAssignableFrom(clazz)) {
                unRegister((T) clazz);
            }
        }
    }

    default void unRegisterPackage(T packageClass) {
        unRegisterPackage(packageClass.getPackage().getName());
    }

    default void registerAll(ClassCollector collector) {
        for (Class clazz : collector.collectAll()) {
            if (getObjectClass().isAssignableFrom(clazz)) {
                register((T) clazz);
            } else {
                LOGGER.warn(clazz.getName() + " can not be registered because collector type is: " + getObjectClass().getName());
            }
        }
    }

}
