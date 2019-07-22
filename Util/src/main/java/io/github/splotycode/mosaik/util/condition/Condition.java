package io.github.splotycode.mosaik.util.condition;

import java.util.function.Predicate;

/**
 * @deprecated use {@link Predicate}
 */
@Deprecated
public interface Condition<T> extends Predicate<T> {

   boolean check(T item);

    @Override
    default boolean test(T t) {
        return check(t);
    }
}
