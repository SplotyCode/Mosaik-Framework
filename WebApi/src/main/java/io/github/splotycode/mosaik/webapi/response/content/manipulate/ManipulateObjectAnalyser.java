package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.pattern.Pattern;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManipulateObjectAnalyser {

    private static HashMap<Class, AnalysedObject> objects = new HashMap<>();

    public static AnalysedObject getObject(Object object) {
        Class clazz = object.getClass();
        return objects.computeIfAbsent(clazz, k -> new AnalysedObject(clazz));
    }

    @Getter
    public static class AnalysedObject {

        private Map<String, Field> fields = new HashMap<>();
        private Map<String, Method> methods;

        private Class clazz;

        private AnalysedObject(Class clazz) {
            this.clazz = clazz;
            for (Field field : ReflectionUtil.getAllFields(clazz)) {
                String name = field.getName();
                if (field.isAnnotationPresent(Pattern.class)) {
                    name = field.getAnnotation(Pattern.class).value();
                }
                fields.put(name, field);
            }
            methods = ReflectionUtil.getMethodFields(clazz);
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
