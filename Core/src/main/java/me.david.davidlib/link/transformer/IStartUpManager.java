package me.david.davidlib.link.transformer;

import me.david.davidlib.iui.INamedTaskBar;

public interface IStartUpManager {

    void researchTasks();
    void executeTasks();

    INamedTaskBar currentProcess();

}
