package io.github.splotycode.mosaik.util.task;

import java.util.concurrent.locks.Lock;

public interface Task extends Runnable {

    default String displayName() {
        return getClass().getName();
    }

    default TaskType getType() {
        return TaskType.REPEATABLE;
    }

    Lock getLock();

    default boolean runSimultaneity() {
        return false;
    }

    void onInstall(TaskExecutor executor);

}