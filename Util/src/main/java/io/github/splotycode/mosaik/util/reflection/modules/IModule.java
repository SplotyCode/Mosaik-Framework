package io.github.splotycode.mosaik.util.reflection.modules;

import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public interface IModule {

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
        for (String checker : loadChecker()) {
            if (!ReflectionUtil.clazzExists(checker)) {
                throw new ModuleNotInClassPathException("Module: " + getDisplayName() + " is not in the classpath (" + checker + ")");
            }
        }
        for (IModule dependency : getAllDependencies()) {
            for (String checker : dependency.loadChecker()) {
                if (!ReflectionUtil.clazzExists(checker)) {
                    throw new ModuleNotInClassPathException(getDisplayName() +  " could not be loaded because its dependency " + dependency.getDisplayName() + " is not in classpath (" + checker + ")");
                }
            }
        }
    }

}
