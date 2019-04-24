package io.github.splotycode.mosaik.util.event;

/**
 * Represents a object that can be canceled.
 * Most commonly used for events
 */
public interface Cancelable {

    boolean isCanceled();
    void setCanceled(boolean cancel);

}
