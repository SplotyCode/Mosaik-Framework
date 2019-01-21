package io.github.splotycode.mosaik.webapi.handler.anotation.handle;

import io.github.splotycode.mosaik.webapi.handler.anotation.parameter.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UseResolver {

    Class<? extends ParameterResolver> value();

}
