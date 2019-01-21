package io.github.splotycode.mosaik.database.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to declare a that this Column is Primary
 * https://www.w3schools.com/sql/sql_primarykey.asp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Primary {

}
