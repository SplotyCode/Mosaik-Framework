package io.github.splotycode.mosaik.util.event;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventPriority {

    public static final int HIGHEST = Integer.MAX_VALUE;
    public static final int HIGH = 10000;
    public static final int NORMAL = 0;
    public static final int LOW = -10000;
    public static final int LOWEST = Integer.MIN_VALUE;

}
