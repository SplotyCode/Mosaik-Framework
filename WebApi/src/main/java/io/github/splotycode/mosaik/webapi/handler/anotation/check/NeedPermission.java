package io.github.splotycode.mosaik.webapi.handler.anotation.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NeedPermission {

    /**
     * Normally "" means that the Session just need to exists
     */
    String value() default "";

}
