package me.david.davidlib.database.connection.sql;

public abstract class AbstractSQLConnection<T extends AbstractSQLConnection> extends SQLDriverConnection<T> {

    /**
     * @return the sql prefix/protocol
     */
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
