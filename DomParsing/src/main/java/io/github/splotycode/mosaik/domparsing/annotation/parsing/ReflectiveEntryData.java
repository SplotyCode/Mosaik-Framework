package io.github.splotycode.mosaik.domparsing.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.DomValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReflectiveEntryData {

    private Class clazz;
    private Map<String, String> linkToFields = new HashMap<>();

    public ReflectiveEntryData(Class clazz) {
        this.clazz = clazz;
        for (Field field : clazz.getDeclaredFields()) {
            String name = field.getName();
            if (field.isAnnotationPresent(DomValue.class)) {
                name = field.getAnnotation(DomValue.class).value();
            }
            linkToFields.put(name, field.getName());
        }
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return linkToFields.entrySet();
    }

    public String getFieldName(String key) {
        return linkToFields.get(key);
    }

}
