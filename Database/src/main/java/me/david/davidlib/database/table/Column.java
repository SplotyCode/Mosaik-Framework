package me.david.davidlib.database.table;

import me.david.davidlib.database.databasetypes.Casandra;
import me.david.davidlib.database.databasetypes.SQL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation Represents a Database Field
 * Or In Some SQL Database Rows
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * The name of the Field
     * If no name is set it will use the Name of the Class
     */
    String name() default "";

    /**
     * The Type of the Field is only interesting for SQL.
     * By Default we will try to convert the Java Type to the Database Type
     * SQL Example:
     *   String <-> Varchar
     */
    @SQL @Casandra ColumnType type() default ColumnType.NONE;

    @SQL @Casandra int[] typeParameters() default {};

}
