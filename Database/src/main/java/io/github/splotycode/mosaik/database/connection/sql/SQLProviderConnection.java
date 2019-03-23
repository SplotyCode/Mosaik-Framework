package io.github.splotycode.mosaik.database.connection.sql;

import java.sql.Connection;
import java.util.function.Supplier;

public class SQLProviderConnection extends SQLDriverConnection<SQLProviderConnection> {

    private Supplier<Connection> connectionSupplier;

    public SQLProviderConnection() {

    }

    public SQLProviderConnection(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public Connection getConnection() {
        return connectionSupplier == null ? null : connectionSupplier.get();
    }

    @Override
    public SQLProviderConnection connect(String host, String user, String password, String database, int port) {
        return this;
    }

    @Override
    public SQLProviderConnection connect(String host, String database, int port) {
        return this;
    }
}
