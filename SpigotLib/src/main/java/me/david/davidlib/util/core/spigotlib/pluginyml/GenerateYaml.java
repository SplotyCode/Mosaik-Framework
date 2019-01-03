package me.david.davidlib.util.core.spigotlib.pluginyml;

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

    String[] dependencies() default {};
    String[] softDependencies() default {};

}
