package me.david.davidlib.spigotlib.pluginyml;

import org.apache.commons.lang.ArrayUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateYaml {

    double version();

    String name();

    String author();

    String[] dependencys() default {};
    String[] softDependencys() default {};

}
