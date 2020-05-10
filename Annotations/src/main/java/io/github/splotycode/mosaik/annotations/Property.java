package io.github.splotycode.mosaik.annotations;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * This Annotation represents a Property of a Entity
 * Use {@link Properties} if you want so use multiple Properties for one Field
 */
@Target(ElementType.FIELD)
@Repeatable(Properties.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Name of the property if empty the {@link Field#getName()} will be used
     */
    String value() default "";

    /**
     * List of service names that this property will apply to
     * If empty this Property will apply to all Services
     */
    String[] services() default {};
    /**
     * List of service classes that this property will apply to
     * If empty this Property will apply to all Services
     */
    Class[] servicesClasses() default {};


    /**
     * List of service names that should ignore this Property
     */
    String[] disabledServices() default {};
    /**
     * List of service classes that should ignore this Property
     */
    Class[] disabledServicesClasses() default {};

    /**
     * Should we use the getter method if found
     */
    boolean useGetter() default true;
    /**
     * Should we use the setter method if found
     */
    boolean useSetter() default true;

    /**
     * What exactly this methods does depends on the implementation
     *
     * Here are a few examples:
     * 1) ArgParser: When there is not an argument matching the name ({@link Property#value()}
     * 2) Serialization: If the source does not provide an value for this Property
     */
    boolean required() default false;

}
