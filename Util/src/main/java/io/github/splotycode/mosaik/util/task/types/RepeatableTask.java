package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Getter;

public class RepeatableTask extends AbstractTask {

    @Getter protected volatile long lastReset;
    @Getter protected final long delay;

    public RepeatableTask(String displayName, long delay) {
        super(displayName);
        this.delay = delay;
        checkDelay();
    }

    private void checkDelay() {
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay might be greater then delay");
        }
    }

    public RepeatableTask(Runnable runnable, long delay) {
        super(runnable);
        this.delay = delay;
    }

    public RepeatableTask(String displayNme, Runnable runnable, long delay) {
        super(displayNme, runnable);
        this.delay = delay;
    }

    public RepeatableTask(long delay) {
        this.delay = delay;
    }

    @Override
    public long executionCheck(TaskExecutor.TaskExecutionContext ctx) {
        long end = delay + lastReset;
        long currentDelay = end - System.currentTimeMillis();
        if (currentDelay > 0) {
            return currentDelay;
        }
        ctx.exec();
        reset();
        return delay;
    }

    public void reset() {
        lastReset = System.currentTimeMillis();
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        super.onInstall(executor);
        reset();
    }

}
