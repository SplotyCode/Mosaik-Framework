package me.david.davidlib.annotations.skippedpath;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this Annotation is you want skip Paths
 * For example if you use a apache library and you want that DavidLib don't scan it you could use this code
 * <code>@SkipPath("org.apache")</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SkipPath {

    String[] value();

}
