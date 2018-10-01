package me.david.davidlib.utils;

public final class ThreadUtil {

    public static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
