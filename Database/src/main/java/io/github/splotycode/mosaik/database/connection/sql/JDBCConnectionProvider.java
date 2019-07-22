package io.github.splotycode.mosaik.database.connection.sql;

import io.github.splotycode.mosaik.database.connection.ConnectionException;
import io.github.splotycode.mosaik.database.connection.config.DefaultConifgConnectionProvider;

import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionProvider extends DefaultConifgConnectionProvider<JDBCConnectionProvider, JDBCConnection> {

    private JDBCConnection connection;
    private String sqlPrefix;

    public JDBCConnectionProvider(String sqlPrefix) {
        this.sqlPrefix = sqlPrefix;
    }

    @Override
    public JDBCConnectionProvider connect(String string) {
        if (!string.startsWith("jdbc")) {
            string = "jdbc:" + sqlPrefix + string;
        }
        try {
            connection = new JDBCConnection(DriverManager.getConnection(string), false);
        } catch (SQLException ex) {
            throw new ConnectionException("SQL Exception on Connecting with DriverManager", ex);
        }
        return this;
    }

    @Override
    public JDBCConnectionProvider connect(String host, String user, String password, String database, int port) {
        return connect("jdbc:" + sqlPrefix + "://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&serverTimezone=UTC");
    }

    @Override
    public JDBCConnectionProvider connect(String host, String database, int port) {
        return connect("jdbc:" + sqlPrefix + "://" + host + ":" + port + "/" + database + "?serverTimezone=UTC");
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Override
    public int getDefaultPort() {
        return 3306;
    }

    @Override
    public JDBCConnection provide() {
        return connection;
    }
}
