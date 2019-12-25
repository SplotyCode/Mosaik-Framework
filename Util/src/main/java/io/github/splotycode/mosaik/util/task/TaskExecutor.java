package io.github.splotycode.mosaik.util.task;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages and instructs Tasks to a ExecutorService
 * @see Task
 */
public class TaskExecutor extends Thread {

    private final ConcurrentHashMap<Long, Task> runningTasks = new ConcurrentHashMap<>();
    @Getter private final ExecutorService service;
    private final AtomicLong currentID = new AtomicLong();
    private volatile boolean interrupt = false;

    public TaskExecutor(ExecutorService service) {
        this.service = service;
        start();
    }

    public TaskExecutor(int threads) {
        this(Executors.newFixedThreadPool(threads));
    }

    private final TaskExecutionContext HELPER = new TaskExecutionContext();

    /**
     * This Object is only instanced once because eof GC.
     * It is used to check the execution of a check.
     */
    public class TaskExecutionContext {

        private Map.Entry<Long, Task> currentPair;

        /**
         * Stops the "current" task
         */
        public void remove() {
            stopTask(currentPair.getKey());
        }

        /**
         * Executes "current" task
         */
        public void exec() {
            Task task = currentPair.getValue();
            if (task.runSimultaneity()) {
                service.submit(task);
            } else {
                service.submit(() -> {
                    try {
                        task.run();
                    } finally {
                        task.getLock().unlock();
                        synchronized (this) {
                            notify();
                        }
                    }
                });
            }
        }

    }

    @Override
    public void run() {
        while (!interrupt) {
            long minimumWait = Long.MAX_VALUE;
            for (Map.Entry<Long, Task> pair : runningTasks.entrySet()) {
                Task task = pair.getValue();
                if (!task.runSimultaneity() || task.getLock().tryLock()) {
                    HELPER.currentPair = pair;
                    long minSleep = task.executionCheck(HELPER);
                    minimumWait = Math.min(minimumWait, minSleep);
                }
            }
            try {
                synchronized (this) {
                    wait(minimumWait == Long.MAX_VALUE ? 0 : minimumWait);
                }
            } catch (InterruptedException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    /**
     * After this no tasks will run.
     * Tasks that got already pushed to the ExecutorService might still run.
     */
    public void interruptQueue() {
        interrupt = true;
    }

    /**
     * Returns all tasks that this TaskExecutor tracks.
     * At soon as a tasks gets added with {@link TaskExecutor#runTask(Task)} it will be in this collection,
     * even though it might to run for 2 hours.
     */
    public Collection<Task> getRunningTasks() {
        return runningTasks.values();
    }

    /**
     * Starts a task
     * @return the unique task id so you can stop the task later
     */
    public long runTask(Task task) {
        try {
            long id = currentID.getAndIncrement();

            task.onInstall(this);
            runningTasks.put(id, task);

            return id;
        } finally {
            synchronized (this) {
                notify();
            }
        }
    }

    /**
     * Stops a task
     * @param id the unique task id that you got from {@link TaskExecutor#runTask(Task)} )}
     */
    public void stopTask(long id) {
        Task task = runningTasks.remove(id);
        if (task != null) {
            task.onUnInstall(this);
        }
    }

    /**
     * Creates a new task with the default conductor
     * and then calls {@link TaskExecutor#runTask(Task)}
     */
    public void runTask(Class<? extends Task> taskClazz) {
        try {
            Task task = taskClazz.newInstance();
            runTask(task);
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

}
