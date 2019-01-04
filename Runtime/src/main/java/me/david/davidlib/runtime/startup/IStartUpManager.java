package me.david.davidlib.runtime.startup;

import me.david.davidlib.iui.INamedTaskBar;

public interface IStartUpManager {

    void researchTasks();
    void executeTasks();

    boolean running();
    default boolean initialised() {
        return true;
    }

    INamedTaskBar currentProcess();

}
