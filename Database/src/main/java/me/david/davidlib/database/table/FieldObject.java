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

    public FieldObject(Column column, Field field) {
        this.column = column;
        this.field = field;

        if (column.name().equals("")) {
            name = field.getName();
        } else {
            name = column.name();
        }
    }
}
