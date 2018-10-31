package me.david.webapi.handler.anotation.handle;

import me.david.webapi.handler.anotation.transform.Transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UseTransformer {

    Class<? extends Transformer> value();

}
