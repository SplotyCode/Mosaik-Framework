package me.david.davidlib.database.connection;

import me.david.davidlib.database.Database;
import me.david.davidlib.utils.StringUtils;

public interface Connection<T extends Connection> {

    boolean isConnected();
    void disconnect();

    T connect(String string);
    T connect(String host, String database);
    T connect(String host, String user, String password, String database);
    T connect(String host, String user, String password, String database, int port);
    T connect(String host, String database, int port);

    int getDefaultPort();

    default void makeDefault() {
        Database.getInstance().setDefaultConnection(this);
    }

}
