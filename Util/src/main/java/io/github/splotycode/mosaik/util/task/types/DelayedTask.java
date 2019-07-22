package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Getter;
import lombok.Setter;

public class DelayedTask extends AbstractTask {

    @Getter @Setter private volatile long start, delay;
    
    public DelayedTask(long delay) {
        this.delay = delay;
    }

    public DelayedTask(Runnable runnable, long delay) {
        super(runnable);
        this.delay = delay;
    }

    public DelayedTask(String displayNme, long delay) {
        super(displayNme);
        this.delay = delay;
    }

    public DelayedTask(String displayNme, Runnable runnable, long delay) {
        super(displayNme, runnable);
        this.delay = delay;
    }

    @Override
    public long executionCheck(TaskExecutor.TaskExecutionContext ctx) {
        long end = delay + start;
        long delay = end - System.currentTimeMillis();
        if (delay > 0) {
            return delay;
        }
        ctx.remove();
        ctx.exec();
        return -1;
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        super.onInstall(executor);
        start = System.currentTimeMillis();
    }
}
