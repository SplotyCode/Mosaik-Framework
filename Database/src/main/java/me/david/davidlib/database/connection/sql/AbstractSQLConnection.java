package me.david.davidlib.database.connection.sql;

import me.david.davidlib.database.connection.AbstractConnection;

public abstract class AbstractSQLConnection<T extends AbstractSQLConnection> extends SQLDriverConnection<T> {

    protected abstract String getSQLPrefix();

    @Override
    public T connect(String host, String user, String password, String database, int port) {
        return connect("jdbc:" + getSQLPrefix() + "://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
    }

    @Override
    public T connect(String host, String database, int port) {
        return connect("jdbc:" + getSQLPrefix() + "://" + host + ":" + port + "/" + database);
    }

}
