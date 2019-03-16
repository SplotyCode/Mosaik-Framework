package io.github.splotycode.mosaik.util.reflection.annotation.parameter;

import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UseResolver {

    Class<? extends ParameterResolver> value();

}
