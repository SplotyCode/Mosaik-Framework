package io.github.splotycode.mosaik.util.reflection.modules;

public interface IModule {

    IModule[] getDependecies();

    String getDisplayName();

    String[] loadChecker();

}
