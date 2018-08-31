package me.david.davidlib.database.repo;

import com.google.common.reflect.TypeToken;
import me.david.davidlib.database.connection.Connection;
import me.david.davidlib.database.table.Column;
import me.david.davidlib.database.table.FieldObject;
import me.david.davidlib.database.table.Primary;
import me.david.davidlib.database.table.Table;
import me.david.davidlib.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractExecutor<T> implements TableExecutor<T> {

    private Class<?> clazz = new TypeToken<T>(getClass()){}.getRawType();
    private Table table;
    private String name;

    private HashMap<String, FieldObject> fields = new HashMap<>();
    private FieldObject primary = null;

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
