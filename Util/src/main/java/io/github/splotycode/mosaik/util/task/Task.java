package io.github.splotycode.mosaik.util.task;

import java.util.concurrent.locks.Lock;

/**
 * A Task that cam be called from {@link TaskExecutor}.
 * Also the when to call logic is in done by tasks
 */
public interface Task extends Runnable {

    /**
     * Human readable task name
     */
    default String displayName() {
        return getClass().getName();
    }

    /**
     * Lock that is used when the task can not be ran simultaneity
     */
    Lock getLock();

    /**
     * Checks if or when the task should be executed
     * @return the amount of milliseconds when this method can be called again.
     *              Or -1 if it does not need to be called again. Nota that this will not
     *              unregister the task
     */
    long executionCheck(TaskExecutor.TaskExecutionContext ctx);

    /**
     * Can this task be ran simultaneity.
     * Performance will increased if running simultaneity is allowed.
     */
    default boolean runSimultaneity() {
        return false;
    }

    /**
     * Called when this task is registered to the TaskExecutor
     */
    void onInstall(TaskExecutor executor);

    /**
     * Called when this task is unregistered to the TaskExecutor
     */
    void onUnInstall(TaskExecutor executor);

}