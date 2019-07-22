package io.github.splotycode.mosaik.database.connection.sql;

import io.github.splotycode.mosaik.database.connection.Connection;
import io.github.splotycode.mosaik.database.connection.ConnectionException;
import lombok.Getter;

import java.sql.SQLException;

@Getter
public class JDBCConnection implements Connection {

    private java.sql.Connection connection;
    private boolean shouldClose;

    public JDBCConnection(java.sql.Connection connection, boolean shouldClose) {
        this.connection = connection;
        this.shouldClose = shouldClose;
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            throw new ConnectionException("SQL Exception on isClosed(): " + ex.getMessage(), ex);
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
            connection.close();
            } catch (SQLException ex) {
                throw new ConnectionException("SQL Exception on close(): " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void disconnectIfNecessary() {
        if (shouldClose) {
            disconnect();
        }
    }
}
