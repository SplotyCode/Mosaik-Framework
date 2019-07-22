package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.util.Collection;

public interface IShutdownManager {

    void simulateShutdown();
    Collection<Runnable> getShutdownTasks();

    void addShutdownTask(Runnable runnable);
    void addShutdownTask(Thread thread);

    void addFirstShutdownTask(Runnable runnable);
    void addFirstShutdownTask(Thread thread);

    void addFirstTaskExecutor(TaskExecutor executor);
    void addLastTaskExecutor(TaskExecutor executor);

}
