package me.david.davidlib.startup;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BootContext {

    private String[] args;
    private long startTime;

    public long getUpTime() {
        return System.currentTimeMillis() - startTime;
    }

}
