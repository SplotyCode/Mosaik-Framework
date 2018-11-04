package me.david.davidlib.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShutdownManager implements IShutdownManager {

    private Set<Runnable> tasks = new HashSet<>();
    private Set<Thread> threads = new HashSet<>();

    public ShutdownManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threads.forEach(Thread::start);
            tasks.forEach(Runnable::run);
        }, "DavidLib Shutdown Thread"));
    }

    @Override
    public void addShutdownTask(Runnable runnable) {
        tasks.add(runnable);
    }

    @Override
    public void addShutdownTask(Thread thread) {
        threads.add(thread);
    }
}
