package io.github.splotycode.mosaik.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds two Objects like Map.Entry.
 */
@Getter
@Setter
public class Pair <A, B> {

    /**
     * A empty pair
     */
    public static final Pair<?, ?> EMPTY = new Pair<>(null, null);

    /**
     * Returns a empty pair
     */
    public static <A, B> Pair<A, B> getEmpty() {
        return (Pair<A, B>) EMPTY;
    }

    private A one;
    private B two;

    public Pair(A one, B two) {
        this.one = one;
        this.two = two;
    }

}
