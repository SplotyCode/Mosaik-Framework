package me.david.davidlib.database.connection.sql;

import me.david.davidlib.database.connection.AbstractConnection;
import me.david.davidlib.database.connection.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class SQLDriverConnection extends AbstractConnection {

    private Connection connection;

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
    public AbstractConnection connect(String string) {
        try {
            connection = DriverManager.getConnection(string);
        } catch (SQLException ex) {
            throw new ConnectionException("SQL Exception on Connecting with DriverManager", ex);
        }
        return this;
    }

    @Override
    public int getDefaultPort() {
        return 3306;
    }
}
