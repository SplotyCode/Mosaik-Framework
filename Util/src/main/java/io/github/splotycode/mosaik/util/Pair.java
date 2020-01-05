package io.github.splotycode.mosaik.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Holds two Objects like Map.Entry.
 */
@Data
@NoArgsConstructor
public class Pair <A, B> implements Map.Entry<A, B>, Cloneable {

    /**
     * A empty pair
     */
    public static final Pair<?, ?> EMPTY = new Pair<>(null, null);

    /**
     * Returns a empty pair
     */
    @SuppressWarnings("unchecked")
    public static <A, B> Pair<A, B> getEmpty() {
        return (Pair<A, B>) EMPTY;
    }

    private A one;
    private B two;

    public Pair(A one, B two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public Pair<A, B> clone() {
        return new Pair<>(one, two);
    }

    @Override
    public A getKey() {
        return one;
    }

    @Override
    public B getValue() {
        return two;
    }

    @Override
    public B setValue(B value) {
        return two;
    }

}
