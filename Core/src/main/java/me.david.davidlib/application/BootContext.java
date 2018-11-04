package me.david.davidlib.application;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BootContext {

    private String[] args;
    private long startTime;

}
