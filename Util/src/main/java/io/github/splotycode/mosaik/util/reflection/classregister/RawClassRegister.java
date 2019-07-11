package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;

public interface RawClassRegister<T extends Class> {

    Logger LOGGER = Logger.getInstance(RawClassRegister.class);

    void register(T clazz);
    void unRegister(T clazz);

    Class<T> getObjectClass();

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

    default void unRegisterPackage(String path) {
        for (Class clazz : ClassFinderHelper.getUserClasses()) {
            if (clazz.getName().startsWith(path) && getObjectClass().isAssignableFrom(clazz)) {
                unRegister((T) clazz);
            }
        }
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
