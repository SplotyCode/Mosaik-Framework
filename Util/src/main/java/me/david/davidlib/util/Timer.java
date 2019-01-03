package me.david.davidlib.util;

public class Timer implements Cloneable {

    private long start;

    public Timer start(){
        start = System.currentTimeMillis();
        return this;
    }

    public long getDelay(){
        return System.currentTimeMillis()-start;
    }

    public boolean hasReached(long reached){
        return getDelay() > reached;
    }

    @Override
    public Timer clone() {
        try {
            return (Timer) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
