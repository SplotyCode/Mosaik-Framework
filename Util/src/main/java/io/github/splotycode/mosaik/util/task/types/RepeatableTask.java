package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.TaskType;
import lombok.Getter;

public class RepeatableTask extends AbstractTask {

    @Getter private volatile long delay, lastReset;

    public RepeatableTask(String displayName, long delay) {
        super(TaskType.REPEATABLE, displayName);
        this.delay = delay;
    }

    public RepeatableTask(long delay) {
        super(TaskType.REPEATABLE);
        this.delay = delay;
    }

    public void reset() {
        lastReset = System.currentTimeMillis();
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        reset();
    }

}
