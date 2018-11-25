package me.david.webapi.response.content.manipulate;

import lombok.Getter;
import me.david.davidlib.utils.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainpulateObjectAnalyser {

    private static HashMap<String, AnalysedObject> objects = new HashMap<>();

    public static AnalysedObject getObject(Object object) {
        String className = object.getClass().getSimpleName();
        AnalysedObject data = objects.get(className);
        if (data == null) {
            data = new AnalysedObject(object);
            objects.put(className, data);
            //if (object.getClass().isAnnotationPresent(Pattern.class))
            //    objects.put(object.getClass().getAnnotation(Pattern.class).value(), data);
        }
        return data;
    }

    public static Object getValue(String name, Object object) {
        try {
            Field field = object.getClass().getField(getObject(object).fields.get(name));
            field.setAccessible(true);
            return field.get(object);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Getter
    public static class AnalysedObject {

        private Map<String, String> fields = new HashMap<>();

        private AnalysedObject(Object object) {
            for (Field field : ReflectionUtil.getAllFields(object.getClass())) {
                String name = field.getName();
                if (field.isAnnotationPresent(Pattern.class)) {
                    name = field.getAnnotation(Pattern.class).value();
                }
                fields.put(field.getName(), name);
            }
        }

    }
}
