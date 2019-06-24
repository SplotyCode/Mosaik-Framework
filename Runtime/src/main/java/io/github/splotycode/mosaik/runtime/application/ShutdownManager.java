package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.util.task.Task;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.CompressingTask;
import lombok.Getter;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ShutdownManager implements IShutdownManager {

    @Getter private LinkedList<Runnable> shutdownTasks = new LinkedList<>();

    public ShutdownManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::simulateShutdown, "Mosaik Shutdown Thread"));
    }

    @Override
    public void simulateShutdown() {
        shutdownTasks.forEach(Runnable::run);
    }

    private Runnable getTaskExecutorRunnable(TaskExecutor taskExecutor) {
        return () -> {
            taskExecutor.interruptQueue();
            ExecutorService service = taskExecutor.getService();
            service.shutdown();
            try {
                if (!service.awaitTermination(1, TimeUnit.HOURS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
            }

            for (Task task : taskExecutor.getRunningTasks()) {
                if (task instanceof CompressingTask) {
                    CompressingTask cTask = (CompressingTask) task;
                    synchronized (cTask) {
                        if (cTask.execOnShutdown() && cTask.getCurrentWait() != -1) {
                            cTask.run();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void addFirstTaskExecutor(TaskExecutor executor) {
        shutdownTasks.addFirst(getTaskExecutorRunnable(executor));
    }

    @Override
    public void addLastTaskExecutor(TaskExecutor executor) {
        shutdownTasks.addLast(getTaskExecutorRunnable(executor));
    }

    @Override
    public void addShutdownTask(Runnable runnable) {
        shutdownTasks.addLast(runnable);
    }

    @Override
    public void addShutdownTask(Thread thread) {
        shutdownTasks.addLast(thread::start);
    }

    @Override
    public void addFirstShutdownTask(Runnable runnable) {
        shutdownTasks.addFirst(runnable);
    }

    @Override
    public void addFirstShutdownTask(Thread thread) {
        shutdownTasks.addFirst(thread::start);
    }

}
