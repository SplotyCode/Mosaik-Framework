package io.github.splotycode.mosaik.util.event;

public interface Cancelable {

    boolean isCanceled();
    void setCanceled(boolean cancel);

}
