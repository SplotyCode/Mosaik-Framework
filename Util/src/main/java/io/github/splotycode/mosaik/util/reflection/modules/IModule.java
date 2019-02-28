package io.github.splotycode.mosaik.util.reflection.modules;

import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collection;

public interface IModule {

    IModule[] getDependencies();

    String getDisplayName();

    String[] loadChecker();

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
        for (IModule dependency : getAllDependencies()) {
            for (String checker : dependency.loadChecker()) {
                if (!ReflectionUtil.clazzExists(checker)) {
                    return false;
                }
            }
        }
        return true;
    }

}
