package io.github.splotycode.mosaik.webapi.handler.anotation.handle;

import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UseTransformer {

    Class<? extends ValueTransformer> value();

}
