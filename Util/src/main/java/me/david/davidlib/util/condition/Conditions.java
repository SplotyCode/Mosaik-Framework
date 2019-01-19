package me.david.davidlib.util.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public final class Conditions {

    public static final Predicate TRUE = item -> true;
    public static final Predicate FALSE = item -> false;
    public static final Predicate NOT_NULL = Objects::nonNull;

    public static <T> Predicate<T> reverse(Predicate<T> condition) {
        return item -> !condition.test(item);
    }


    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>)TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>)FALSE;
    }

    public static <T> Predicate<T> notNull() {
        return (Predicate<T>)NOT_NULL;
    }

    public static <T> Predicate<T> constant(boolean value) {
        return (Predicate<T>)(value ? TRUE : FALSE);
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... conditions) {
        return item -> Arrays.stream(conditions).allMatch(condition -> condition.test(item));
    }

    public static <T> Predicate<T> and(Collection<Predicate<T>> conditions) {
        return item -> conditions.stream().allMatch(condition -> condition.test(item));
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... conditions) {
        return item -> Arrays.stream(conditions).anyMatch(condition -> condition.test(item));
    }

    public static <T> Predicate<T> or(Collection<Predicate<T>> conditions) {
        return item -> conditions.stream().anyMatch(condition -> condition.test(item));
    }

}
