package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManipulateObjectAnalyser {

    private static HashMap<String, AnalysedObject> objects = new HashMap<>();

    public static AnalysedObject getObject(Object object) {
        String className = object.getClass().getSimpleName();
        //if (object.getClass().isAnnotationPresent(Pattern.class))
        //    objects.put(object.getClass().getAnnotation(Pattern.class).value(), data);
        return objects.computeIfAbsent(className, k -> new AnalysedObject(object));
    }

    @Getter
    public static class AnalysedObject {

        private Map<String, Field> fields = new HashMap<>();
        private Map<String, Method> methods = new HashMap<>();

        private AnalysedObject(Object object) {
            for (Field field : ReflectionUtil.getAllFields(object.getClass())) {
                String name = field.getName();
                if (field.isAnnotationPresent(Pattern.class)) {
                    name = field.getAnnotation(Pattern.class).value();
                }
                fields.put(name, field);
            }
            for (Method method : ReflectionUtil.getAllMethods(object.getClass())) {
                HandleAsField anno = method.getAnnotation(HandleAsField.class);
                if (anno != null) {
                    if (method.getParameterCount() != 0) throw new ManipulationException("Methods with @HandleAsField may not have parameters");
                    String name = method.getName();
                    if (!anno.name().isEmpty()) {
                        name = anno.name();
                    }
                    methods.put(name, method);
                }
            }
        }

        public Object getValueByName(Object obj, String varName) throws IllegalAccessException, InvocationTargetException {
            Method method = methods.get(varName);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(obj);
            }
            Field field = fields.get(varName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
            throw new ManipulationException("Could not find value for " + varName + " valid names are " + StringUtil.join(Stream.concat(fields.keySet().stream(), methods.keySet().stream()).collect(Collectors.toSet()), ", "));
        }

    }
}
