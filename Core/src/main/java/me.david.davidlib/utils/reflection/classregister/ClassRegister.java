package me.david.davidlib.utils.reflection.classregister;

import com.google.common.reflect.ClassPath;

import java.io.IOException;

public interface ClassRegister<T> {

    void register(T obj);
    void unRegister(T obj);

    default void register(Class<? extends T> clazz) {
        try {
            register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    default void unRegister(Class<? extends T> clazz) {
        try {
            unRegister(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    default void register(String clazz) {
        try {
            register((T) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    default void unRegister(String clazz) {
        try {
            unRegister((T) Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Class<T> getObjectClass();

    default void registerPackage(String path) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive(path)) {
                Class<?> clazz = classInfo.load();
                if (getObjectClass().isAssignableFrom(clazz)) {
                    register((Class<? extends T>) clazz);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    default void unRegisterPackage(String path) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive(path)) {
                Class<?> clazz = classInfo.load();
                if (getObjectClass().isAssignableFrom(clazz)) {
                    unRegister((Class<? extends T>) clazz);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
