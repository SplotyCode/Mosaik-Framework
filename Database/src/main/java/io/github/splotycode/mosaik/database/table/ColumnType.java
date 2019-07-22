package io.github.splotycode.mosaik.database.table;

import io.github.splotycode.mosaik.database.annotation.MSSQL;
import io.github.splotycode.mosaik.database.annotation.MySQL;
import io.github.splotycode.mosaik.database.annotation.SQL;
import io.github.splotycode.mosaik.database.annotation.Casandra;

/**
 * All SQL ColumnType's
 *
 * @see Column
 */
@Casandra
@SQL
@SuppressWarnings("unused")
public enum ColumnType {

    /* String data types */
    @MSSQL @MySQL
    CHAR(1),
    @MSSQL @MySQL
    VARCHAR(1),
    @MSSQL @MySQL
    TEXT(1),
    @MSSQL
    N_CHAR,
    @MSSQL
    N_VARCHAR(1),
    @MSSQL
    N_TEXT,
    @MSSQL @MySQL
    BINARY(1),
    @MSSQL @MySQL
    VARBINARY(1),
    @MSSQL
    IMAGE,
    @MySQL
    TINYBLOB,
    @MySQL
    TINYTEXT,
    @MySQL
    BLOB(1),
    @MySQL
    MEDIUMTEXT,
    @MySQL
    MEDIUMBLOB,
    @MySQL
    LONGTEXT,
    @MySQL
    LONGBLOB,
    @MySQL
    ENUM(65535 ),
    @MySQL
    SET(64),

    /* Numeric data types */
    @MSSQL @MySQL
    BIT(1),
    @MSSQL @MySQL
    TINYINT(1),
    @MSSQL @MySQL
    SMALLINT(1),
    @MySQL
    BOOL,
    @MySQL
    BOOLEAN,
    @MSSQL @MySQL
    INT(1),
    @MySQL
    INTEGER(1),
    @MSSQL @MySQL
    BIGINT(1),
    @MySQL
    MEDIUMINT(1),
    @MSSQL @MySQL
    DECIMAL(2),
    @MSSQL
    NUMERIC(2),
    @MSSQL
    SMALL_MONEY,
    @MSSQL
    MONEY,
    @MSSQL @MySQL
    FLOAT(1),
    @MSSQL
    REAL,
    @MySQL
    DOUBLE(2),
    @MySQL
    DOUBLE_PRECISION(2),
    @MySQL
    DEC(2),

    /* Date and Time data types */
    @MSSQL @MySQL
    DATE,
    @MSSQL @MySQL
    DATETIME(1),
    @MSSQL
    DATETIME_2,
    @MSSQL
    SMALL_DATETIME,
    @MSSQL @MySQL
    TIMESTAMP(1),
    @MSSQL @MySQL
    TIME(1),
    @MSSQL
    DATETIMEOFFSET,
    @MySQL
    YEAR,

    /* Other data types */
    @MSSQL
    TABLE,
    @MSSQL
    CURSOR,
    @MSSQL
    XML,
    @MSSQL
    SQL_VARIANT,
    @MSSQL
    UNIQUEIDENTIFIER,

    /** Depends Most times it try's to get the ColumnType automatically */
    NONE;

    private final int parameterSize;

    ColumnType() {
        parameterSize = 0;
    }

    ColumnType(int parameterSize) {
        this.parameterSize = parameterSize;
    }
}
