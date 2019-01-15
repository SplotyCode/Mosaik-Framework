package me.david.davidlib.util.condition;

import java.util.Arrays;
import java.util.Objects;

public final class Conditions {

    public static final Condition TRUE = item -> true;
    public static final Condition FALSE = item -> false;
    public static final Condition NOT_NULL = Objects::nonNull;

    public static <T> Condition<T> reverse(Condition<T> condition) {
        return item -> !condition.check(item);
    }


    public static <T> Condition<T> alwaysTrue() {
        return (Condition<T>)TRUE;
    }

    public static <T> Condition<T> alwaysFalse() {
        return (Condition<T>)FALSE;
    }

    public static <T> Condition<T> notNull() {
        return (Condition<T>)NOT_NULL;
    }

    public static <T> Condition<T> constant(boolean value) {
        return (Condition<T>)(value ? TRUE : FALSE);
    }

    @SafeVarargs
    public static <T> Condition<T> and(Condition<T>... conditions) {
        return item -> Arrays.stream(conditions).allMatch(condition -> condition.check(item));
    }

    @SafeVarargs
    public static <T> Condition<T> or(Condition<T>... conditions) {
        return item -> Arrays.stream(conditions).anyMatch(condition -> condition.check(item));
    }

}
