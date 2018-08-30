package me.david.davidlib.database.table;

import me.david.davidlib.database.databasetypes.Casandra;
import me.david.davidlib.database.databasetypes.SQL;

@Casandra
@SQL
public enum RowType {

    CHAR,
    VARCHAR,
    TINYTEXT,
    TEXT,
    BLOB,
    MEDIUMTEXT,
    MEDIUMBLOB,
    LONGTEXT,
    LONGBLOB,

    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    BIGINT,

    DATE,
    DATETIME,
    TIMESTAMP,
    TIME,
    YEAR,

    /** Depends */
    NONE
}
