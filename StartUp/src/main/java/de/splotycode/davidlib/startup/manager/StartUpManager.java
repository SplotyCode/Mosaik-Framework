package de.splotycode.davidlib.startup.manager;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import de.splotycode.davidlib.startup.starttask.StartTaskExecutor;
import lombok.Setter;
import me.david.davidlib.iui.INamedTaskBar;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.startup.IStartUpManager;

public class StartUpManager implements IStartUpManager {

    @Setter private boolean running = false;

    @Override
    public void researchTasks() {
        StartTaskExecutor.getInstance().findAll(true);
    }

    @Override
    public void executeTasks() {
        StartTaskExecutor.getInstance().runAll(LinkBase.getInstance().getLink(Links.STARTUP_ENVIRONMENT_CHANGER));
    }

    @Override
    public boolean running() {
        return running;
    }

    @Override
    public INamedTaskBar currentProcess() {
        return StartUpProcessHandler.getInstance().current();
    }
}
