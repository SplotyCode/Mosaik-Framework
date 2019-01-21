package io.github.splotycode.mosaik.database.connection.impl;

import io.github.splotycode.mosaik.database.connection.sql.AbstractSQLConnection;

public class MySQLConnection extends AbstractSQLConnection<MySQLConnection> {

    @Override
    protected String getSQLPrefix() {
        return "mysql";
    }

}
