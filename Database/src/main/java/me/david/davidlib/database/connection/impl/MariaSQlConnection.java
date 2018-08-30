package me.david.davidlib.database.connection.impl;

import me.david.davidlib.database.connection.sql.AbstractSQLConnection;

public class MariaSQlConnection extends AbstractSQLConnection {

    @Override
    protected String getSQLPrefix() {
        return "mariadb";
    }

}
