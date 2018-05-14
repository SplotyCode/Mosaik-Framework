package me.david.davidlib.event;

public interface Cancelable {

    boolean isCanceled();
    void setCanceled(boolean cancel);

}
