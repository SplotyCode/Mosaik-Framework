package me.david.webapi.handler.anotation.check;

import me.david.webapi.handler.anotation.parameter.ParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AddTransforwer {

    Class<? extends ParameterResolver>[] value();

}
