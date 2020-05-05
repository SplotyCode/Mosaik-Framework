package io.github.splotycode.mosaik.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation represents an Object Head
 * Use {@link Entities} if you want so use multiple Entity configurations for one Object
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    /**
     * Name of the Entity if empty the {@link Class#getName()} will be used
     */
    String value() default "";

    /**
     * If true it will not scan for methods/fields on th super class
     */
    boolean ignoreSuper();

    /**
     * List of service names that this entity configuration will apply to
     * If empty this entity configuration will apply to all Services
     */
    String[] services() default {};
    /**
     * List of service classes that this entity configuration will apply to
     * If empty this entity configuration will apply to all Services
     */
    Class[] servicesClasses() default {};

    /**
     * List of service names that should ignore this entity configuration
     */
    String[] disabledServices() default {};
    /**
     * List of service classes that should ignore this entity configuration
     */
    Class[] disabledServicesClasses() default {};

}
