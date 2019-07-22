package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.Task;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public abstract class AbstractTask implements Task {

    @Setter protected String displayNme;
    protected Runnable runnable;
    private Lock lock = new ReentrantLock();
    private volatile boolean used;

    public AbstractTask() {
        this((Runnable) null);
    }

    public AbstractTask(Runnable runnable) {
        this.runnable = runnable;
        displayNme = getClass().getName();
    }

    public AbstractTask(String displayNme) {
        this.displayNme = displayNme;
    }

    public AbstractTask(String displayNme, Runnable runnable) {
        this.displayNme = displayNme;
        this.runnable = runnable;
    }

    @Override
    public void onInstall(TaskExecutor executor) {
        if (used) {
            throw new IllegalStateException("Already used");
        }
        used = true;
    }

    @Override
    public void onUnInstall(TaskExecutor executor) {
        used = false;
    }

    @Override
    public void run() {
        if (runnable != null) {
            runnable.run();
        } else throw new NoTaskImplementedException();
    }

    public static class NoTaskImplementedException extends RuntimeException {

        public NoTaskImplementedException() {
        }

        public NoTaskImplementedException(String s) {
            super(s);
        }

        public NoTaskImplementedException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public NoTaskImplementedException(Throwable throwable) {
            super(throwable);
        }

        public NoTaskImplementedException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

}
