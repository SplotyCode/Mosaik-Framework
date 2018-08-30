package me.david.davidlib.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection extends AbstractConnection {

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
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AbstractConnection connect(String host, String user, String password, String database, int port) {
        return null;
    }

    @Override
    public AbstractConnection connect(String host, String database, int port) {
        return null;
    }

    @Override
    public int getDefaultPort() {
        return 3306;
    }
}
