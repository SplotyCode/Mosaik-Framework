package io.github.splotycode.mosaik.util.task.types;

import io.github.splotycode.mosaik.util.task.Task;
import io.github.splotycode.mosaik.util.task.TaskType;
import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public abstract class AbstractTask implements Task {

    private TaskType type;
    private String displayNme;
    private Runnable runnable;
    private Lock lock = new ReentrantLock();

    public AbstractTask(TaskType type) {
        this(type, (Runnable) null);
    }

    public AbstractTask(TaskType type, Runnable runnable) {
        this.type = type;
        this.runnable = runnable;
        displayNme = getClass().getName();
    }

    public AbstractTask(TaskType type, String displayNme) {
        this.type = type;
        this.displayNme = displayNme;
    }

    public AbstractTask(TaskType type, String displayNme, Runnable runnable) {
        this.type = type;
        this.displayNme = displayNme;
        this.runnable = runnable;
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
