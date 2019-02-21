package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.TaskType;
import lombok.Getter;
import lombok.Setter;

public class DelayedTask extends AbstractTask {

    @Getter @Setter private volatile long start, delay;
    
    public DelayedTask(long delay) {
        super(TaskType.DELAYED);
        this.delay = delay;
    }

    public DelayedTask(Runnable runnable, long delay) {
        super(TaskType.DELAYED, runnable);
        this.delay = delay;
    }

    public DelayedTask(String displayNme, long delay) {
        super(TaskType.DELAYED, displayNme);
        this.delay = delay;
    }

    public DelayedTask(String displayNme, Runnable runnable, long delay) {
        super(TaskType.DELAYED, displayNme, runnable);
        this.delay = delay;
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        start = System.currentTimeMillis();
    }
}
