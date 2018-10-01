package me.david.davidlib.gamesngine.tick;

import java.util.ArrayList;

public class TickExecutorList extends ArrayList<ExecutorTask> implements TickExecutor {

    @Override
    public void runTick() {
        preTick();
        forEach(ExecutorTask::exec);
        postTick();
    }

    public void installExecutorTask(ExecutorTask task) {
        if (contains(task)) throw new TickRegistryExcetion("ExecutorTask is already installed");
        add(task);
    }

    public void uninstallExecutorTask(ExecutorTask task) {
        if (!contains(task)) throw new TickRegistryExcetion("ExecutorTask us not installed");
    }

    @Override public void postTick() {}
    @Override public void preTick() {}

}
