package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.TaskType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

public class CompressingTask extends AbstractTask {

    @Getter private long waitDelay;
    @Getter private final AtomicLong currentWait = new AtomicLong(-1);
    private TaskExecutor executor;

    public CompressingTask(long waitDelay) {
        super(TaskType.COMPRESSING);
        this.waitDelay = waitDelay;
    }

    public CompressingTask(Runnable runnable, long waitDelay) {
        super(TaskType.COMPRESSING, runnable);
        this.waitDelay = waitDelay;
    }

    public CompressingTask(String displayNme, long waitDelay) {
        super(TaskType.COMPRESSING, displayNme);
        this.waitDelay = waitDelay;
    }

    public CompressingTask(String displayNme, Runnable runnable, long waitDelay) {
        super(TaskType.COMPRESSING, displayNme, runnable);
        this.waitDelay = waitDelay;
    }

    public void requestUpdate() {
        synchronized (currentWait) {
            if (currentWait.get() == -1) {
                currentWait.set(System.currentTimeMillis());
            }
        }
        executor.notify();
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        this.executor = executor;
    }

}
