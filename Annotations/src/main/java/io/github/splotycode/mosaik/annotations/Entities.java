package io.github.splotycode.mosaik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Combines multiple {@link Entity} configurations to one {@link java.lang.Class}
 * @see Entity
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entities {

    Entity[] value();

}
