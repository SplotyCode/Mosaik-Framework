package io.github.splotycode.mosaik.webapi.handler.anotation.check;

import io.github.splotycode.mosaik.webapi.response.ContentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseContentType {

    ContentType value();

}
