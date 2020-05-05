package io.github.splotycode.mosaik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Combines multiple {@link Property} configurations to one {@link java.lang.reflect.Field}
 * @see Property
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Properties {

    Property[] value();

}
