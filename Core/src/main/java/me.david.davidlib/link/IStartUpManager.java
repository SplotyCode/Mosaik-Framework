package me.david.davidlib.link;

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
