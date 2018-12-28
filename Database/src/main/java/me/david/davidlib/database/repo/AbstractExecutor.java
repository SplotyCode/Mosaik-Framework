package me.david.davidlib.database.repo;

import me.david.davidlib.database.connection.Connection;
import me.david.davidlib.database.table.Column;
import me.david.davidlib.database.table.FieldObject;
import me.david.davidlib.database.table.Primary;
import me.david.davidlib.database.table.Table;
import me.david.davidlib.utils.reflection.ReflectionUtil;

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
