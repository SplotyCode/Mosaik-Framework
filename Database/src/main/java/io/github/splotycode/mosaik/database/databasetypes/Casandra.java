package io.github.splotycode.mosaik.database.databasetypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Symbolises that the method, constructor or type is used for especially for casandra
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Casandra {

}
