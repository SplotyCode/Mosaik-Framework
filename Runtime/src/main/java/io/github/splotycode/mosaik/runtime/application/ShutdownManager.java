package io.github.splotycode.mosaik.runtime.application;

import java.util.LinkedList;

public class ShutdownManager implements IShutdownManager {

    private LinkedList<Runnable> tasks = new LinkedList<>();

    public ShutdownManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> tasks.forEach(Runnable::run), "Mosaik Shutdown Thread"));
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
