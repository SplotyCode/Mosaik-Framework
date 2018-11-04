package me.david.davidlib.application;

public interface IShutdownManager {

    void addShutdownTask(Runnable runnable);
    void addShutdownTask(Thread thread);

}
