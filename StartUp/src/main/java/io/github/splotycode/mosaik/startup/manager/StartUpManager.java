package io.github.splotycode.mosaik.startup.manager;

import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.startup.starttask.StartTaskExecutor;
import io.github.splotycode.mosaik.iui.INamedTaskBar;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.IStartUpManager;

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

    public void finished() {
        running = true;
    }

    @Override
    public INamedTaskBar currentProcess() {
        return StartUpProcessHandler.getInstance().current();
    }
}
