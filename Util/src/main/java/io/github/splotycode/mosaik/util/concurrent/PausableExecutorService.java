package io.github.splotycode.mosaik.util.concurrent;

import io.github.splotycode.mosaik.util.ExceptionUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PausableExecutorService extends DelegatedExecutorService{

    public boolean paused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();
    private Phaser phaser = new Phaser(1);

    public PausableExecutorService(ExecutorService executor) {
        super(executor);
    }

    @Override
    public void execute(Runnable command) {
        pauseLock.lock();
        try {
            while (paused)
                unpaused.await();
        } catch (InterruptedException ie) {
            ExceptionUtil.throwRuntime(ie);
        } finally {
            pauseLock.unlock();
        }
        phaser.register();
        super.execute(() -> {
            try {
                command.run();
            } finally {
                phaser.arriveAndDeregister();
            }
        });
        pauseLock.lock();
    }

    public void resume() {
        pauseLock.lock();
        try {
            paused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            paused = true;
        } finally {
            pauseLock.unlock();
        }
        phaser.arriveAndAwaitAdvance();
    }

}
