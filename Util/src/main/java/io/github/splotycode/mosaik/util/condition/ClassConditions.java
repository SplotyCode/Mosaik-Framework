package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.annotations.Disabled;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassConditions {

    public static final Predicate<Class> NEED_ABSTRACT = item -> Modifier.isAbstract(item.getModifiers());
    public static final Predicate<Class> NOT_ABSTRACT = Conditions.reverse(NEED_ABSTRACT);

    public static final Predicate<Class> NO_DISABLE_ANNOTATION = unallowedAnnotation(Disabled.class);

    public static final Predicate<Class> MUST_BE_CLASS = item -> !item.isEnum() && !item.isInterface();

    public static <C extends Class> Predicate<C> mustBeClass() {
        return (Predicate<C>) MUST_BE_CLASS;
    }

    public static Predicate<Class> isInPackage(String packageName) {
        return clazz -> clazz.getName().startsWith(packageName);
    }

    public static Predicate<Class> instanceOf(Class clazz) {
        return clazz::isInstance;
    }

    public static Predicate<Class> assignable(Class<?> clazz) {
        return clazz::isAssignableFrom;
    }

    public static Predicate<Class> needAnnotation(Class<? extends Annotation> annotation) {
        return item -> item.isAnnotationPresent(annotation);
    }

    public static Predicate<Class> unallowedAnnotation(Class<? extends Annotation> annotation) {
        return item -> !item.isAnnotationPresent(annotation);
    }

    public static Predicate<Class> anyMethod(Predicate<Method> methodCondition) {
        return item -> Arrays.stream(item.getDeclaredMethods()).anyMatch(methodCondition::test);
    }

    public static Predicate<Class> allMethods(Predicate<Method> methodCondition) {
        return item -> Arrays.stream(item.getDeclaredMethods()).anyMatch(methodCondition::test);
    }


}
