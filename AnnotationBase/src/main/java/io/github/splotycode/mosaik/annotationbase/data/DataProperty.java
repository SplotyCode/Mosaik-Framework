package io.github.splotycode.mosaik.annotationbase.data;

import io.github.splotycode.mosaik.annotations.Property;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class DataProperty {

    static DataProperty fromMethod(DataEntity entity, Method method) {
        return new DataProperty(entity, PropertyReader.fromMethod(method));
    }

    static DataProperty fromField(DataEntity entity, Field field, Property property) {
        boolean hasProperty = property != null;
        Optional<Method> getter = hasProperty && property.useGetter() ? ReflectionUtil.findGetter(field) : Optional.empty();
        Optional<Method> setter = hasProperty && property.useSetter() ? ReflectionUtil.findSetter(field) : Optional.empty();
        PropertyReader reader = getter.map(PropertyReader::fromMethod)
                .orElseGet(() -> PropertyReader.fromField(field));
        PropertyWriter writer = setter.map(PropertyWriter::writeToMethod)
                .orElseGet(() -> PropertyWriter.fromField(field));
        return new DataProperty(entity, hasProperty && property.required(), reader, writer);
    }

    private DataEntity entity;

    private boolean required;

    private PropertyReader reader;
    private PropertyWriter writer;

    public DataProperty(DataEntity entity, PropertyReader reader) {
        this.entity = entity;
        this.reader = reader;
    }

    public Object read(Object entity) {
        return reader.readValue(Objects.requireNonNull(entity, "entity"));
    }

    public boolean isReadOnly() {
        return writer == null;
    }

    public void write(Object entity, Object value) {
        if (isReadOnly()) {
            throw new UnsupportedOperationException("This Property is read only");
        }
        writer.writeValue(Objects.requireNonNull(entity, "entity"), value);
    }
}
