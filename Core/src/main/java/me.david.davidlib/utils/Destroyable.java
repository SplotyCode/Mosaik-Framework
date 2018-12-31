package me.david.davidlib.utils;

/**
 * This interface Represents an Object that can be destroyed
 * This has many use cases such as a Window Object that can be destroyed, a server that shut down etc
 */
public interface Destroyable {

    /**
     * Called when the Object should be destroyed
     */
    void destroy();

}
