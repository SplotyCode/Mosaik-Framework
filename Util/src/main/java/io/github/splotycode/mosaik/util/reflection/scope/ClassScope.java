package io.github.splotycode.mosaik.util.reflection.scope;

import io.github.splotycode.mosaik.util.reflection.classpath.ClassPathVisitor;

import java.util.function.Predicate;

public interface ClassScope extends Predicate<String>, ClassPathVisitor {

    boolean test(Class<?> clazz);

    void ignorePath(String path);
    void addPath(String path);

    default void ignorePaths(String... paths) {
        for (String path : paths) {
            ignorePath(path);
        }
    }

    default void addPaths(String... paths) {
        for (String path : paths) {
            addPath(path);
        }
    }

    default void ignorePaths(Iterable<String> paths) {
        for (String path : paths) {
            ignorePath(path);
        }
    }

    default void addPaths(Iterable<String> paths) {
        for (String path : paths) {
            addPath(path);
        }
    }
}
