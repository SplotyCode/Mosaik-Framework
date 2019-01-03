package me.david.webapi.handler.anotation.handle;

import me.david.davidlib.runtimeapi.transformer.ValueTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UseTransformer {

    Class<? extends ValueTransformer> value();

}
