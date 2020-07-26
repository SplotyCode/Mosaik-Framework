package io.github.splotycode.mosaik.runtime.module;

import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public interface IModule {

    /**
     * Only holds dependencies that are required
     */
    IModule[] getDependencies();

    String getDisplayName();

    String[] loadChecker();

    default Collection<String> getAllLoadChecker() {
        Collection<String> loadCheckers = new ArrayList<>(Arrays.asList(loadChecker()));
        for (IModule dependency : getAllDependencies()) {
            loadCheckers.addAll(Arrays.asList(dependency.loadChecker()));
        }
        return loadCheckers;
    }

    default Collection<IModule> getAllDependencies() {
        return getAllDependencies(new ArrayList<>(), this);
    }

    default Collection<IModule> getAllDependencies(Collection<IModule> list, IModule base) {
        for (IModule module : base.getDependencies()) {
            list.add(module);
            list = getAllDependencies(list, module);
        }
        return list;
    }

    default boolean isLoaded() {
        for (String checker : getAllLoadChecker()) {
            if (!ReflectionUtil.clazzExists(checker)) {
                return false;
            }
        }
        return true;
    }

    default void checkLoaded() {
        String loadError = getLoadError();
        if (loadError != null) {
            throw new ModuleNotInClassPathException(loadError);
        }
    }

    default String getLoadError() {
        for (String checker : loadChecker()) {
            if (!ReflectionUtil.clazzExists(checker)) {
                return "Module: " + getDisplayName() + " is not in the classpath (" + checker + ")";
            }
        }
        for (IModule dependency : getAllDependencies()) {
            for (String checker : dependency.loadChecker()) {
                if (!ReflectionUtil.clazzExists(checker)) {
                    return getDisplayName() +  " could not be loaded because its dependency " + dependency.getDisplayName() + " is not in classpath (" + checker + ")";
                }
            }
        }
        return null;
    }

}
