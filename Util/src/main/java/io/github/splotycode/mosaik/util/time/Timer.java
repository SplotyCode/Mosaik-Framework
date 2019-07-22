package io.github.splotycode.mosaik.util.time;

import io.github.splotycode.mosaik.util.ExceptionUtil;

/**
 * Used to measure timing
 */
public class Timer implements Cloneable {

    private long start;

    /**
     * Starts the timer
     * @return this
     */
    public Timer start(){
        start = System.currentTimeMillis();
        return this;
    }

    /**
     * Returns the delay in milliseconds
     */
    public long getDelay(){
        return System.currentTimeMillis()-start;
    }

    /**
     * Checks if this timer is already running for x time
     * @param reached the reached time in milliseconds
     */
    public boolean hasReached(long reached){
        return getDelay() > reached;
    }

    @Override
    public Timer clone() {
        try {
            return (Timer) super.clone();
        } catch (CloneNotSupportedException e) {
            ExceptionUtil.throwRuntime(e);
        }
        return null;
    }
}
