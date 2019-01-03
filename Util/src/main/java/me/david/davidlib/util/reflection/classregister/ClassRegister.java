package me.david.davidlib.util.reflection.classregister;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public interface ClassRegister<T> {

    void register(T obj);
    void unRegister(T obj);

    Collection<T> getAll();

    default void register(Class<? extends T> clazz) {
        try {
            register(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
        for (T obj : new ArrayList<>(getAll())) {
            if (obj.getClass().getSimpleName().startsWith(path)) {
                unRegister(obj);
            }
        }
    }

}
