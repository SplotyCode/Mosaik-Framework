package io.github.splotycode.mosaik.annotationbase.data;

import io.github.splotycode.mosaik.annotations.Property;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class DataProperty {

    public static DataProperty fromMethod(DataEntity entity, Method method) {
        return new DataProperty(entity, PropertyReader.fromMethod(method, entity.getObject()));
    }

    public static DataProperty fromField(DataEntity entity, String name, Field field, Property property) {
        Object object = entity.getObject();

        Optional<Method> getter = property.useGetter() ? ReflectionUtil.findGetter(field) : Optional.empty();
        Optional<Method> setter = property.useSetter() ? ReflectionUtil.findSetter(field) : Optional.empty();
        PropertyReader reader = getter.map(method -> PropertyReader.fromMethod(method, object))
                .orElseGet(() -> PropertyReader.fromField(field, entity.getObject()));
        PropertyWriter writer = setter.map(method -> PropertyWriter.writeToMethod(method, object))
                .orElseGet(() -> PropertyWriter.fromField(field, entity.getObject()));
        return new DataProperty(entity, property.required(), reader, writer);
    }

    private DataEntity entity;

    private boolean required;

    private PropertyReader reader;
    private PropertyWriter writer;

    public DataProperty(DataEntity entity, PropertyReader reader) {
        this.entity = entity;
        this.reader = reader;
    }

    public Object read() {
        return reader.readValue();
    }

    public boolean isReadOnly() {
        return writer == null;
    }

    public void write(Object value) {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("This Property is read only");
        }
        writer.writeValue(value);
    }

}
