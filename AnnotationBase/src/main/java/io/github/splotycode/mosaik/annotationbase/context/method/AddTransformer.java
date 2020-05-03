package io.github.splotycode.mosaik.annotationbase.context.method;

import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AddTransformer {

    Class<? extends ParameterResolver>[] value();

}
