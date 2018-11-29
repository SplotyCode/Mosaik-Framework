package me.david.davidlib.link;

import me.david.davidlib.iui.INamedTaskBar;

public interface IStartUpManager {

    void researchTasks();
    void executeTasks();

    boolean running();
    default boolean initlised() {
        return true;
    }

    INamedTaskBar currentProcess();

}
