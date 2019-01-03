package me.david.davidlib.runtimeapi.application;

public interface IShutdownManager {

    void addShutdownTask(Runnable runnable);
    void addShutdownTask(Thread thread);

}
