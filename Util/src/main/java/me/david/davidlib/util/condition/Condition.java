package me.david.davidlib.util.condition;

import java.util.function.Predicate;

public interface Condition<T> extends Predicate<T> {

   boolean check(T item);

    @Override
    default boolean test(T t) {
        return check(t);
    }
}
