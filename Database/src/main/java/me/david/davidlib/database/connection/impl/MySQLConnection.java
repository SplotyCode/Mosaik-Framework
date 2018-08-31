package me.david.davidlib.database.connection.impl;

import me.david.davidlib.database.connection.sql.AbstractSQLConnection;

public class MySQLConnection extends AbstractSQLConnection<MySQLConnection> {

    @Override
    protected String getSQLPrefix() {
        return "mysql";
    }

}
