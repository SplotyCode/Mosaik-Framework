package io.github.splotycode.mosaik.annotations.visibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

/**
 * Specifies that a Element is Visible
 * @see io.github.splotycode.mosaik.annotations.AnnotationHelper#isVisible(VisibilityLevel, AnnotatedElement)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Visible {

}
