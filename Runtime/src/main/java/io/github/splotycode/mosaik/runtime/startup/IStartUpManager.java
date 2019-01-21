package io.github.splotycode.mosaik.runtime.startup;

import io.github.splotycode.mosaik.iui.INamedTaskBar;

public interface IStartUpManager {

    void researchTasks();
    void executeTasks();

    boolean running();
    default boolean initialised() {
        return true;
    }

    INamedTaskBar currentProcess();

}
