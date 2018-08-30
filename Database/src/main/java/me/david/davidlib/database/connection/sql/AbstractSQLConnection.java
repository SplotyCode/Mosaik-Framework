package me.david.davidlib.database.connection.sql;

import me.david.davidlib.database.connection.AbstractConnection;

public abstract class AbstractSQLConnection extends SQLDriverConnection {

    protected abstract String getSQLPrefix();

    @Override
    public AbstractConnection connect(String host, String user, String password, String database, int port) {
        return connect("jdbc:" + getSQLPrefix() + "://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
    }

    @Override
    public AbstractConnection connect(String host, String database, int port) {
        return connect("jdbc:" + getSQLPrefix() + "://" + host + ":" + port + "/" + database);
    }

}
