package me.david.davidlib.database.table;

import lombok.*;

import java.lang.reflect.Field;

@EqualsAndHashCode
@Getter
@Setter
public class FieldObject {

    private Column column;
    private Field field;
    private String name;

    private boolean autoIncrement,
                    notNull,
                    primary;

    public FieldObject(Column column, Field field) {
        this.column = column;
        this.field = field;

        if (column.name().equals("")) {
            name = field.getName();
        } else {
            name = column.name();
        }

        autoIncrement = field.isAnnotationPresent(AutoIncrement.class);
        notNull = field.isAnnotationPresent(NotNull.class);
        primary = field.isAnnotationPresent(Primary.class);
    }
}
