package io.github.splotycode.mosaik.annotations.priority;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Symbolises that the class has the lowest priority
 * Equals to <code>@Priority(Integer.MIN_VALUE)</code>
 * @see Priority
 * @see First
 * @see AnnotationHelper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Last {
    
}
