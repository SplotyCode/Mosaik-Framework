package me.david.davidlib.util.reflection;

import me.david.davidlib.util.AlmostBoolean;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReflectionUtil {

    private static CallerClass callerClazz = new CallerClass();

    /**
     * @deprecated use {@link ClassCollector} instead
     */
    @Deprecated
    public static boolean validClass(Class<?> clazz, Class<?> other, boolean noAbstraction, boolean disableAnnotation) {
        /*return other.isAssignableFrom(clazz) &&
                !clazz.isInterface() && !clazz.isEnum() &&
                (!noAbstraction || !Modifier.isAbstract(clazz.getModifiers())) &&
                (!disableAnnotation || !clazz.isAnnotationPresent(Disabled.class));*/
        return ClassCollector.newInstance()
                .setAbstracation(AlmostBoolean.fromBoolean(!noAbstraction))
                .setOnlyClasses(true)
                .setNoDisableds(disableAnnotation)
                .setNeedAssignable(other).check(clazz);
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

    public static Type[] getGenerics(Class clazz) {
        return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
    }

    public static boolean isAssignable(Class<?> parent, Class<?> child) {
        return parent.isAssignableFrom(child) || samePrimitive(parent, child);
    }

    public static boolean samePrimitive(Class<?> one, Class<?> two) {
        return samePrimitive(one, two, "int", "Integer") ||
               samePrimitive(one, two, "float", "Float") ||
               samePrimitive(one, two, "double", "Double") ||
               samePrimitive(one, two, "char", "Character") ||
               samePrimitive(one, two, "long", "Long") ||
               samePrimitive(one, two, "short", "Short");
    }

    private static boolean samePrimitive(Class one, Class two, String primiClass, String clazz) {
        String oneName = one.getSimpleName(),
               twoName = two.getSimpleName();
        if (one.isPrimitive() && oneName.equalsIgnoreCase(primiClass) && twoName.equalsIgnoreCase(clazz)) return two.isArray() == one.isArray();
        if (two.isPrimitive() && twoName.equalsIgnoreCase(primiClass) && oneName.equalsIgnoreCase(clazz)) return two.isArray() == one.isArray();
        return false;
    }

    public static boolean methodExists(Class<?> clazz, String method) {
        try {
            clazz.getMethod(method);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static boolean clazzExists(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
