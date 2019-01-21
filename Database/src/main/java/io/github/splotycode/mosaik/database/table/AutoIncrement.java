package io.github.splotycode.mosaik.database.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this Column is AutoIncrement
 * https://www.w3schools.com/sql/sql_autoincrement.asp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoIncrement {

}
