package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Getter;

/**
 * This Task tries to combine multiple calls into one call.
 * The task will be executed if
 * a) Your first request since task execution longer ago then maxDelay or
 * b) The last request is longer ago then waitDelay
 */
public class CompressingTask extends AbstractTask {

    @Getter private final long waitDelay;
    @Getter private final long maxDelay;
    @Getter private volatile long currentWait = -1;
    @Getter private volatile long firstWait = -1;
    private TaskExecutor executor;

    public CompressingTask(long waitDelay, long maxDelay) {
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(Runnable runnable, long waitDelay, long maxDelay) {
        super(runnable);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(String displayNme, long waitDelay, long maxDelay) {
        super(displayNme);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(String displayNme, Runnable runnable, long waitDelay, long maxDelay) {
        super(displayNme, runnable);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    /**
     * Request an update.
     * This might trigger the task depending on how often when you called this method and what you set
     * waitDelay and maxDelay
     */
    public synchronized void requestUpdate() {
        long time = System.currentTimeMillis();
        if (firstWait == -1) {
            firstWait = time;
        }
        currentWait = time;
        executor.notify();
    }

    @Override
    public synchronized long executionCheck(TaskExecutor.TaskExecutionContext ctx) {
        if (currentWait != -1) {
            long end = currentWait + waitDelay;
            long delay = end - System.currentTimeMillis();
            if (delay > 0 && firstWait + maxDelay < System.currentTimeMillis()) {
                return delay;
            }
            firstWait = currentWait = -1;
            ctx.exec();
        }
        return -1;
    }

    /**
     * When there is a request but not enough to trigger the task should the task be executed on shutdown.
     * NOTE: Without Runtime and StartUp you have to build this yourself.
     * If you want to use this feature you have to register it in the global or application shutdownManager
     */
    public boolean execOnShutdown() {
        return false;
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        super.onInstall(executor);
        this.executor = executor;
    }

    @Override
    public void onUnInstall(TaskExecutor executor) {
        super.onUnInstall(executor);
        this.executor = null;
    }
}
