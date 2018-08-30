package me.david.davidlib.database.connection.impl;

import me.david.davidlib.database.connection.sql.AbstractSQLConnection;

public class MySQLConnection extends AbstractSQLConnection {

    @Override
    protected String getSQLPrefix() {
        return "mysql";
    }

}
