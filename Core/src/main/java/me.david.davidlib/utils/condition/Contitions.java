package me.david.davidlib.utils.condition;

import java.util.Objects;

public final class Contitions {

    public static final Condition TRUE = item -> true;
    public static final Condition FALSE = item -> false;
    public static final Condition NOT_NULL = Objects::nonNull;


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

}
