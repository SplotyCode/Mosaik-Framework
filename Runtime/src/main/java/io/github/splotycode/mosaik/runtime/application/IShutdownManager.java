package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.util.task.TaskExecutor;

public interface IShutdownManager {

    void addShutdownTask(Runnable runnable);
    void addShutdownTask(Thread thread);

    void addFirstShutdownTask(Runnable runnable);
    void addFirstShutdownTask(Thread thread);

    void addFirstTaskExecutor(TaskExecutor executor);
    void addLastTaskExecutor(TaskExecutor executor);

}
