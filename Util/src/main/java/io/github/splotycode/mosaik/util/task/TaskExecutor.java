package io.github.splotycode.mosaik.util.task;

import io.github.splotycode.mosaik.util.task.types.CompressingTask;
import io.github.splotycode.mosaik.util.task.types.DelayedTask;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class TaskExecutor extends Thread {

    private ConcurrentHashMap<Long, Task> runningTasks = new ConcurrentHashMap<>();
    @Getter private ExecutorService service;
    private AtomicLong currentID = new AtomicLong();
    private boolean interupt = false;
    private ConcurrentHashMap<Future, Task> futures = new ConcurrentHashMap<>();

    public TaskExecutor(ExecutorService service) {
        this.service = service;
        start();
    }

    @Override
    public void run() {
        while (true) {
            if (interupt) break;
            for (Map.Entry<Future, Task> pair : futures.entrySet()) {
                Future future = pair.getKey();
                if (future.isCancelled() || future.isDone()) {
                    pair.getValue().getLock().unlock();
                    futures.remove(pair.getKey());
                }
            }
            futures.clear();

            long minimumWait = 0;
            for (Map.Entry<Long, Task> pair : runningTasks.entrySet()) {
                Task task = pair.getValue();
                if (!task.runSimultaneity() || task.getLock().tryLock()) {
                    switch (task.getType()) {
                        case DELAYED: {
                            long end = ((DelayedTask) task).getDelay() + ((DelayedTask) task).getStart();
                            long delay = end - System.currentTimeMillis();
                            if (delay > 0) {
                                minimumWait = Math.min(minimumWait, delay);
                            } else {
                                exec(task);
                                runningTasks.remove(pair.getKey());
                            }
                            break;
                        } case REPEATABLE: {
                            RepeatableTask dTask = (RepeatableTask) task;
                            long end = dTask.getDelay() + dTask.getLastReset();
                            long delay = end - System.currentTimeMillis();
                            if (delay > 0) {
                                minimumWait = Math.min(minimumWait, delay);
                            } else {
                                exec(task);
                                dTask.reset();
                            }
                            break;
                        } case COMPRESSING: {
                            CompressingTask cTask = (CompressingTask) task;
                            synchronized (cTask.getCurrentWait()) {
                                long wait = cTask.getCurrentWait().get();
                                if (wait != -1) {
                                    long end = wait + cTask.getWaitDelay();
                                    long delay = end - System.currentTimeMillis();
                                    if (delay > 0) {
                                        minimumWait = Math.min(minimumWait, delay);
                                    } else {
                                        exec(task);
                                        cTask.getCurrentWait().set(-1);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            try {
                wait(minimumWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void interruptQueue() {
        interupt = true;
    }

    public Collection<Task> getRunningTasks() {
        return runningTasks.values();
    }

    private void exec(Task task) {
        if (task.runSimultaneity()) {
            service.submit(task);
        } else {
            futures.put(service.submit(task), task);
        }
    }

    public void runTask(Task task) {
        task.onInstall(this);
        long id = currentID.getAndIncrement();
        runningTasks.put(id, task);
        notify();
    }

    public void runTask(Class<? extends Task> taskClazz) {
        try {
            Task task = taskClazz.newInstance();
            runTask(task);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
