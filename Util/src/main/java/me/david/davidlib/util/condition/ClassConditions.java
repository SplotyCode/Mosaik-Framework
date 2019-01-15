package me.david.davidlib.util.condition;

import me.david.davidlib.annotations.Disabled;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

public final class ClassConditions {

    public static final Condition<Class> NEED_ABSTRACT = item -> Modifier.isAbstract(item.getModifiers());
    public static final Condition<Class> NOT_ABSTRACT = Conditions.reverse(NEED_ABSTRACT);

    public static final Condition<Class> NO_DISABLE_ANNOTATION = unallowedAnnotation(Disabled.class);

    public static final Condition<Class> MUST_BE_CLASS = item -> !item.isEnum() && !item.isInterface();

    public static Condition<Class> instanceOf(Class clazz) {
        return clazz::isInstance;
    }

    public static <C> Condition<Class<C>> assignable(Class<C> clazz) {
        return clazz::isAssignableFrom;
    }

    public static Condition<Class> needAnnotation(Class<? extends Annotation> annotation) {
        return item -> item.isAnnotationPresent(annotation);
    }

    public static Condition<Class> unallowedAnnotation(Class<? extends Annotation> annotation) {
        return item -> !item.isAnnotationPresent(annotation);
    }


}
