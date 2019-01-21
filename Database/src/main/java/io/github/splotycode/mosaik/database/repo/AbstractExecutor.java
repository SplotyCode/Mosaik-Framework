package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.connection.Connection;
import io.github.splotycode.mosaik.database.table.FieldObject;
import io.github.splotycode.mosaik.database.table.Primary;
import io.github.splotycode.mosaik.database.table.Table;
import io.github.splotycode.mosaik.database.table.Column;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class AbstractExecutor<T, C extends Connection> implements TableExecutor<T, C> {

    protected Class<?> clazz = (Class<?>) ReflectionUtil.getGenerics(getClass())[0];
    protected Table table;
    protected String name;

    protected HashMap<String, FieldObject> fields = new HashMap<>();
    protected FieldObject primary = null;

    public AbstractExecutor() {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RepoException(clazz.getSimpleName() + " need Table anotation");
        }
        table = clazz.getAnnotation(Table.class);

        if (table.name().equals("")) {
            name = clazz.getName();
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
    }

}
