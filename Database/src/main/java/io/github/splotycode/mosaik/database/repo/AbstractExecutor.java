package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import io.github.splotycode.mosaik.database.table.*;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractExecutor<T, C extends ConnectionProvider> implements TableExecutor<T, C> {

    protected Class<?> clazz;
    protected Table table;
    protected String name;

    protected HashMap<String, FieldObject> fields = new HashMap<>();
    protected FieldObject primary = null;

    protected ColumnNameResolver[] allResolvers;

    private void resolveAllResolvers() {
        allResolvers = fields.values().stream()
                .map(obj -> (ColumnNameResolver) obj::getName)
                .toArray(ColumnNameResolver[]::new);
    }

    protected String getValue(String field, T instance) {
        return getValue(fields.get(field), instance);
    }

    protected String getValue(FieldObject fieldObject, T instance) {
        Field field = fieldObject.getField();
        field.setAccessible(true);
        try {
            return TransformerManager.getInstance().transform(field.get(instance), String.class);
        } catch (IllegalAccessException e) {
            throw new RepoException("Could not getvalue for " + field.getName(), e);
        }
    }

    public void setValue(String fieldName, Object value, T instance) throws IllegalAccessException {
        Field field = fields.get(fieldName).getField();
        field.setAccessible(true);
        field.set(instance, TransformerManager.getInstance().transform(value, field.getType()));
    }

    public AbstractExecutor(Class<?> clazz) {
        this.clazz = clazz;
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RepoException(clazz.getSimpleName() + " need Table anotation");
        }
        table = clazz.getAnnotation(Table.class);

        if (table.name().equals("")) {
            name = clazz.getSimpleName();
        } else {
            name = table.name();
        }

        for (Field field : ReflectionUtil.getAllFields(clazz)) {
            if (field.isAnnotationPresent(Column.class)) {
                FieldObject object = new FieldObject(field.getAnnotation(Column.class), field);
                if (fields.containsKey(object.getName())) {
                    throw new RepoException("Column " + object.getName() + " is defined twice");
                }
                fields.put(object.getName(), object);

                if (field.isAnnotationPresent(Primary.class)) {
                    if (primary == null) {
                        primary = object;
                    } else throw new RepoException("Can not have multiple Primary Annotations " + primary.getField().getName() + " and " + object.getField().getName());
                }
            } else if (field.isAnnotationPresent(Primary.class)) {
                throw new RepoException("Can not have @Primary without @Column for field " + field.getName());
            }
        }

        resolveAllResolvers();
    }

}
