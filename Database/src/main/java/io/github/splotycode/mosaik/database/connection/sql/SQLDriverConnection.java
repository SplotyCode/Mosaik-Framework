package io.github.splotycode.mosaik.database.connection.sql;

import io.github.splotycode.mosaik.database.connection.AbstractConnection;
import io.github.splotycode.mosaik.database.connection.ConnectionException;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class SQLDriverConnection<T extends SQLDriverConnection> extends AbstractConnection<T> {

    @Getter private Connection connection;

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            throw new ConnectionException("SQL Exception on isClosed():" + ex.getMessage(), ex);
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new ConnectionException("SQL Exception on close():" + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public T connect(String string) {
        try {
            connection = DriverManager.getConnection(string);
        } catch (SQLException ex) {
            throw new ConnectionException("SQL Exception on Connecting with DriverManager", ex);
        }
        return (T) this;
    }

    @Override
    public int getDefaultPort() {
        return 3306;
    }
}
