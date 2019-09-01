package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.annotations.AnnotationHelper;
import io.github.splotycode.mosaik.annotations.Disabled;
import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * Useful class Conditions
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassConditions {

    /**
     * The Predicate will return true if the given class is abstract
     */
    public static final Predicate<Class> NEED_ABSTRACT = item -> Modifier.isAbstract(item.getModifiers());

    /**
     * The Predicate will return true if the given class is not abstract
     */
    public static final Predicate<Class> NOT_ABSTRACT = Conditions.reverse(NEED_ABSTRACT);


    /**
     * The Predicate will return true if the given AnnotatedElement does not have the {@link Disabled} annotation
     */
    public static final Predicate<AnnotatedElement> NO_DISABLE_ANNOTATION = unallowedAnnotation(Disabled.class);

    /**
     * The Predicate will return true if the given class is a real class
     * E.g: No enums or interfaces
     */
    public static final Predicate<Class> MUST_BE_CLASS = item -> !item.isEnum() && !item.isInterface();

    /**
     * The Predicate will return true if the given class is a real class
     * E.g: No enums or interfaces
     */
    public static <C extends Class> Predicate<C> mustBeClass() {
        return (Predicate<C>) MUST_BE_CLASS;
    }

    /**
     * The Predicate will return true if the given class is in a specific package
     */
    public static Predicate<Class> isInPackage(String packageName) {
        return clazz -> clazz.getName().startsWith(packageName);
    }

    /**
     * The Predicate will return true if the given object is a instance of a specific class
     */
    public static Predicate<Class> instanceOf(Object obj) {
        return clazz -> clazz.isInstance(obj);
    }

    /**
     * The Predicate will return true if the given Class is a instance of a specific object
     */
    public static Predicate<Object> instanceOf(Class clazz) {
        return clazz::isInstance;
    }

    /**
     * The Predicate will return true if the given class is a assignable from a specific class
     */
    public static Predicate<Class> assignable(Class<?> clazz) {
        return clazz::isAssignableFrom;
    }

    /**
     * The Predicate will return true if the given object is a assignable from a specific class
     */
    public static <S> Predicate<S> assignableClass(Class<? extends S> clazz) {
        return s -> clazz.isAssignableFrom(s.getClass());
    }


    /**
     * The Predicate will return true if the given class has the specified annotation
     */
    public static Predicate<AnnotatedElement> needAnnotation(Class<? extends Annotation> annotation) {
        return item -> item.isAnnotationPresent(annotation);
    }

    /**
     * The Predicate will return true if the given class has any of the specified annotation
     */
    public static Predicate<AnnotatedElement> needOneAnnotation(Class<? extends Annotation>... annotations) {
        return item -> Arrays.stream(annotations).anyMatch(item::isAnnotationPresent);
    }

    /**
     * The Predicate will return true if the given class has any of the specified annotation
     */
    public static Predicate<AnnotatedElement> needOneAnnotation(Collection<? extends Class<? extends Annotation>> annotations) {
        return item -> annotations.stream().anyMatch(item::isAnnotationPresent);
    }

    /**
     * The Predicate will return true if the given class has all of the specified annotation
     */
    public static Predicate<AnnotatedElement> needAnnotations(Class<? extends Annotation>... annotations) {
        return item -> Arrays.stream(annotations).allMatch(item::isAnnotationPresent);
    }

    /**
     * The Predicate will return true if the given class has all of the specified annotation
     */
    public static Predicate<AnnotatedElement> needAnnotations(Collection<? extends Class<? extends Annotation>> annotations) {
        return item -> annotations.stream().allMatch(item::isAnnotationPresent);
    }

    /**
     * The Predicate will return true if the given class not has the specified annotation
     */
    public static Predicate<AnnotatedElement> unallowedAnnotation(Class<? extends Annotation> annotation) {
        return item -> !item.isAnnotationPresent(annotation);
    }

    /**
     * The Predicate will return true if the any method of the given class bypasses the filter
     * @param methodCondition the filter for the methods
     */
    public static Predicate<Class> anyMethod(Predicate<Method> methodCondition) {
        return item -> ReflectionUtil.getAllMethods(item).stream().anyMatch(methodCondition);
    }

    /**
     * The Predicate will return true if the all method of the given class bypasses the filter
     * @param methodCondition the filter for the methods
     */
    public static Predicate<Class> allMethods(Predicate<Method> methodCondition) {
        return item -> ReflectionUtil.getAllMethods(item).stream().allMatch(methodCondition);
    }

    /**
     * The Predicate will check if a element is visible
     * @param level on with level should we check te visibility
     * @see io.github.splotycode.mosaik.annotations.AnnotationHelper#isVisible(VisibilityLevel, AnnotatedElement)
     */
    public static Predicate<AnnotatedElement> visisble(VisibilityLevel level) {
        return element -> AnnotationHelper.isVisible(level, element);
    }

}
