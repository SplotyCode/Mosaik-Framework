package io.github.splotycode.mosaik.util.concurrent;

import io.github.splotycode.mosaik.util.ThreadUtil;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@AllArgsConstructor
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicLong FACTORY_NUMBER = new AtomicLong();

    private Supplier<String> nameGenerator;
    private ThreadGroup threadGroup;

    private boolean daemon;
    private int priority;
    private long stackSize;

    public DefaultThreadFactory() {
        this("Factory #" + FACTORY_NUMBER.incrementAndGet());
    }

    public DefaultThreadFactory(String factoryName) {
        this(null, ThreadUtil.getCurrentThreadGroup(), false, Thread.NORM_PRIORITY);
        withFactoryName(factoryName);
    }

    public DefaultThreadFactory(Supplier<String> nameGenerator, ThreadGroup threadGroup, boolean daemon, int priority) {
        this(nameGenerator, threadGroup, daemon, priority, 0);
    }

    public DefaultThreadFactory setNameGenerator(Supplier<String> nameGenerator) {
        this.nameGenerator = nameGenerator;
        return this;
    }

    public DefaultThreadFactory withStaticName(String name) {
        return setNameGenerator(() -> name);
    }

    public DefaultThreadFactory withFactoryName(String factoryName) {
        return setNameGenerator(new Supplier<String>() {

            private AtomicLong threadCount = new AtomicLong();

            @Override
            public String get() {
                return  factoryName + " Thread #" + threadCount.incrementAndGet();
            }
        });
    }

    public DefaultThreadFactory setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public DefaultThreadFactory setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public DefaultThreadFactory setStackSize(long stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public DefaultThreadFactory setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        return this;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, nameGenerator.get(), stackSize);

        thread.setDaemon(daemon);
        thread.setPriority(priority);

        return thread;
    }
}
