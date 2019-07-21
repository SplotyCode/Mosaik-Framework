package io.github.splotycode.mosaik.database.table;

import io.github.splotycode.mosaik.database.databasetypes.SQL;
import io.github.splotycode.mosaik.database.databasetypes.Casandra;

/**
 * All SQL ColumnType's
 *
 * @see Column
 */
@Casandra
@SQL
public enum ColumnType {

    CHAR(1),
    VARCHAR(1),
    TINYTEXT,
    TEXT,
    BLOB,
    MEDIUMTEXT,
    MEDIUMBLOB,
    LONGTEXT,
    LONGBLOB,
    ENUM(65535),

    TINYINT(1),
    SMALLINT(1),
    MEDIUMINT(1),
    INT(1),
    BIGINT(1),
    FLOAT(2),
    DOUBLE(2),
    DECIMAL(2),

    DATE,
    DATETIME,
    TIMESTAMP,
    TIME,
    YEAR,

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
