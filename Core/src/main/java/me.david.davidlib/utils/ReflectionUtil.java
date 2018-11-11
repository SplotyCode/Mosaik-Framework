package me.david.davidlib.utils;

import me.david.davidlib.annotation.Disabled;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReflectionUtil {

    private static CallerClass callerClazz = new CallerClass();

    public static boolean validClass(Class<?> clazz, Class<?> other, boolean noAbstraction, boolean disableAnnotation) {
        return other.isAssignableFrom(clazz) &&
                !clazz.isInterface() && !clazz.isEnum() &&
                (!noAbstraction || !Modifier.isAbstract(clazz.getModifiers())) &&
                (!disableAnnotation || !clazz.isAnnotationPresent(Disabled.class));
    }

    public static Class<?> getCallerClass() {
        return callerClazz.getClassContext()[3];
    }

    public static Class<?> getCallerClass(int deapth) {
        return callerClazz.getClassContext()[3 + deapth];
    }

    public static Class<?>[] getCallerClasses() {
        return callerClazz.getClassContext();
    }

    public static List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static class CallerClass extends SecurityManager {

        @Override
        public Class[] getClassContext() {
            return super.getClassContext();
        }
    }

}
