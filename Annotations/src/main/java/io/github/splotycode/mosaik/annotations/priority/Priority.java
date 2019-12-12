package io.github.splotycode.mosaik.annotations.priority;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Symbolises a specific priority for a class
 * For example it is used to device what StartUp Tasks should run first
 * @see Last
 * @see First
 * @see AnnotationHelper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Priority {

    int LOWEST = Integer.MIN_VALUE;
    int NORMAL = 0;
    int HIGHEST = Integer.MAX_VALUE;

    /**
     * Returns the Priority
     * The higher the important's of the class
     * @return the priority of the class
     */
    int priority() default 0;

}
