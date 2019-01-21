package io.github.splotycode.mosaik.webapi.handler.anotation.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UrlParam {

    String value();
    boolean needed() default true;

}
