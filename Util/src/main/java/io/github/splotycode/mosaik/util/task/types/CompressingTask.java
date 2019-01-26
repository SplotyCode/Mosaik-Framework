package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.TaskType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

public class CompressingTask extends AbstractTask {

    @Getter private long waitDelay;
    @Getter private long maxDelay;
    @Getter private final AtomicLong currentWait = new AtomicLong(-1);
    @Getter private final AtomicLong firstWait = new AtomicLong(-1);
    private TaskExecutor executor;

    public CompressingTask(long waitDelay, long maxDelay) {
        super(TaskType.COMPRESSING);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(Runnable runnable, long waitDelay, long maxDelay) {
        super(TaskType.COMPRESSING, runnable);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(String displayNme, long waitDelay, long maxDelay) {
        super(TaskType.COMPRESSING, displayNme);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public CompressingTask(String displayNme, Runnable runnable, long waitDelay, long maxDelay) {
        super(TaskType.COMPRESSING, displayNme, runnable);
        this.waitDelay = waitDelay;
        this.maxDelay = maxDelay;
    }

    public void requestUpdate() {
        long time = System.currentTimeMillis();
        synchronized (firstWait) {
            if (firstWait.get() == -1) {
                firstWait.set(time);
            }
            currentWait.set(time);
        }
        executor.notify();
    }

    public boolean execOnShutdown() {
        return false;
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        this.executor = executor;
    }

}
