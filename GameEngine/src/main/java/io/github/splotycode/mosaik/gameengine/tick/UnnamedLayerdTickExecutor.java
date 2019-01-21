package io.github.splotycode.mosaik.gameengine.tick;

import java.util.Map;
import java.util.TreeMap;

public class UnnamedLayerdTickExecutor implements TickExecutor {

    private Map<Integer, ExecutorTask> tasks = new TreeMap<>();

    @Override
    public void runTick() {
       preTick();
       tasks.values().forEach(ExecutorTask::exec);
       postTick();
    }

    public void installExecutorTask(int priority, ExecutorTask task) {
        tasks.put(priority, task);
    }

    public void uninstallExecutorTask(int priority) {
        tasks.remove(priority);
    }

    @Override public void preTick() {}
    @Override public void postTick() {}

}
