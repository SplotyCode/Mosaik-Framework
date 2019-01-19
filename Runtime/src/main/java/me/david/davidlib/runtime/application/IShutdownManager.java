package me.david.davidlib.runtime.application;

public interface IShutdownManager {

    void addShutdownTask(Runnable runnable);
    void addShutdownTask(Thread thread);

    void addFirstShutdownTask(Runnable runnable);
    void addFirstShutdownTask(Thread thread);

}
