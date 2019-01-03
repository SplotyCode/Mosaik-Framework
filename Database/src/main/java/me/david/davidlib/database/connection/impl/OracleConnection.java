package me.david.davidlib.database.connection.impl;

import me.david.davidlib.database.connection.sql.SQLDriverConnection;
import me.david.davidlib.database.connection.AbstractConnection;

public class OracleConnection extends SQLDriverConnection {

    @Override
    public AbstractConnection connect(String host, String user, String password, String database, int port) {
        connect("jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ": " + database);
        return this;
    }

    @Override
    public AbstractConnection connect(String host, String database, int port) {
        connect("jdbc:oracle:thin@" + host + ":" + port + ":" + database);
        return this;
    }

    @Override
    public int getDefaultPort() {
        return 1521;
    }

}
