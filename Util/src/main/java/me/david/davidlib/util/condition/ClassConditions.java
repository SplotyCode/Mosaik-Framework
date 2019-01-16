package me.david.davidlib.util.condition;

import me.david.davidlib.annotations.Disabled;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

public final class ClassConditions {

    public static final Condition<Class> NEED_ABSTRACT = item -> Modifier.isAbstract(item.getModifiers());
    public static final Condition<Class> NOT_ABSTRACT = Conditions.reverse(NEED_ABSTRACT);

    public static final Condition<Class> NO_DISABLE_ANNOTATION = unallowedAnnotation(Disabled.class);

    public static final Condition<Class> MUST_BE_CLASS = item -> !item.isEnum() && !item.isInterface();

    public static <C extends Class> Condition<C> mustBeClass() {
        return (Condition<C>) MUST_BE_CLASS;
    }


    public static Condition<Class> instanceOf(Class clazz) {
        return clazz::isInstance;
    }

    public static Condition<Class> assignable(Class<?> clazz) {
        return clazz::isAssignableFrom;
    }

    public static Condition<Class> needAnnotation(Class<? extends Annotation> annotation) {
        return item -> item.isAnnotationPresent(annotation);
    }

    public static Condition<Class> unallowedAnnotation(Class<? extends Annotation> annotation) {
        return item -> !item.isAnnotationPresent(annotation);
    }


}
