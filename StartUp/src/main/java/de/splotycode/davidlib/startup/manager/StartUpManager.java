package de.splotycode.davidlib.startup.manager;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import de.splotycode.davidlib.startup.starttask.StartTaskExecutor;
import me.david.davidlib.iui.INamedTaskBar;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.Links;
import me.david.davidlib.runtime.startup.IStartUpManager;

public class StartUpManager implements IStartUpManager {

    private boolean running = false;

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

    public boolean isRunning() {
        return running;
    }

    @Override
    public INamedTaskBar currentProcess() {
        return StartUpProcessHandler.getInstance().current();
    }
}
