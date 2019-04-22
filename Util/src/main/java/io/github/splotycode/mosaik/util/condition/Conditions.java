package io.github.splotycode.mosaik.util.condition;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * General useful Conditions
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Conditions {

    /**
     * This Predicate will always return true
     */
    public static final Predicate TRUE = item -> true;

    /**
     * This Predicate will always return true
     */
    public static final Predicate FALSE = item -> false;

    /**
     * This Predicate will return true if the checked object is not null
     */
    public static final Predicate NOT_NULL = Objects::nonNull;

    /**
     * Reverses a Predicate
     * @param condition the Predicate you want to reverse
     * @return the reversed predicate
     */
    public static <T> Predicate<T> reverse(Predicate<T> condition) {
        return item -> !condition.test(item);
    }

    /**
     * This Predicate will always return true
     */
    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>)TRUE;
    }

    /**
     * This Predicate will always return false
     */
    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>)FALSE;
    }

    /**
     * This Predicate will return true if the checked object is not null
     */
    public static <T> Predicate<T> notNull() {
        return (Predicate<T>)NOT_NULL;
    }

    /**
     * This Predicate will always return a constant value
     * @param value the constant value
     */
    public static <T> Predicate<T> constant(boolean value) {
        return value ? alwaysTrue() : alwaysFalse();
    }

    /**
     * Create a Predicate from a list of other Predicates.
     * All Sub-Predicates need to be true in order to return true in the created Predicate.
     * @param conditions the Sub-Predicates
     */
    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... conditions) {
        return item -> Arrays.stream(conditions).allMatch(condition -> condition.test(item));
    }

    /**
     * Create a Predicate from a list of other Predicates.
     * All Sub-Predicates need to be true in order to return true in the created Predicate.
     * @param conditions the Sub-Predicates
     */
    public static <T> Predicate<T> and(Collection<Predicate<? super T>> conditions) {
        return item -> conditions.stream().allMatch(condition -> condition.test(item));
    }

    /**
     * Create a Predicate from a list of other Predicates.
     * Any Sub-Predicates need to be true in order to return true in the created Predicate.
     * @param conditions the Sub-Predicates
     */
    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... conditions) {
        return item -> Arrays.stream(conditions).anyMatch(condition -> condition.test(item));
    }

    /**
     * Create a Predicate from a list of other Predicates.
     * Any Sub-Predicates need to be true in order to return true in the created Predicate.
     * @param conditions the Sub-Predicates
     */
    public static <T> Predicate<T> or(Collection<Predicate<T>> conditions) {
        return item -> conditions.stream().anyMatch(condition -> condition.test(item));
    }

}
