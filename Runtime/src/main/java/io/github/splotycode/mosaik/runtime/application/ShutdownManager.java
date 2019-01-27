package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.util.task.Task;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.TaskType;
import io.github.splotycode.mosaik.util.task.types.CompressingTask;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ShutdownManager implements IShutdownManager {

    private LinkedList<Runnable> tasks = new LinkedList<>();

    public ShutdownManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> tasks.forEach(Runnable::run), "Mosaik Shutdown Thread"));
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
                if (task.getType() == TaskType.COMPRESSING) {
                    CompressingTask cTask = (CompressingTask) task;
                    if (cTask.execOnShutdown() && cTask.getCurrentWait().get() != -1) {
                        cTask.run();
                    }
                }
            }
        };
    }

    @Override
    public void addFirstTaskExecutor(TaskExecutor executor) {
        tasks.addFirst(getTaskExecutorRunnable(executor));
    }

    @Override
    public void addLastTaskExecutor(TaskExecutor executor) {
        tasks.addLast(getTaskExecutorRunnable(executor));
    }

    @Override
    public void addShutdownTask(Runnable runnable) {
        tasks.addLast(runnable);
    }

    @Override
    public void addShutdownTask(Thread thread) {
        tasks.addLast(thread::start);
    }

    @Override
    public void addFirstShutdownTask(Runnable runnable) {
        tasks.addFirst(runnable);
    }

    @Override
    public void addFirstShutdownTask(Thread thread) {
        tasks.addFirst(thread::start);
    }

}
