package io.github.splotycode.mosaik.util.condition;

import java.util.function.Predicate;

/**
 * @deprecated use {@link Predicate}
 */
@Deprecated
public interface Processor<T> extends Predicate<T> {

    boolean process(T item);

    @Override
    default boolean test(T t) {
        return process(t);
    }

}
