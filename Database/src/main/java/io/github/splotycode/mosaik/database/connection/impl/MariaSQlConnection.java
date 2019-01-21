package io.github.splotycode.mosaik.database.connection.impl;

import io.github.splotycode.mosaik.database.connection.sql.AbstractSQLConnection;

public class MariaSQlConnection extends AbstractSQLConnection {

    @Override
    protected String getSQLPrefix() {
        return "mariadb";
    }

}
