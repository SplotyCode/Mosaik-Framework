package io.github.splotycode.mosaik.database.connection.config;

import io.github.splotycode.mosaik.database.connection.Connection;
import io.github.splotycode.mosaik.database.connection.ConnectionProvider;

public interface ConfigConnectionProvider<T extends ConnectionProvider, C extends Connection> extends ConnectionProvider<T, C> {

    /**
     * Gets the default port for this connection port
     * @return the port
     */
    int getDefaultPort();

    /**
     * Connects to the Database
     * @param string full connection string
     * @return this connection
     */
    T connect(String string);

    /**
     * Connects to the Database
     * @param host the host or host:port
     * @param database the database name
     * @return this connection
     */
    T connect(String host, String database);

    /**
     * Connects to the Database
     * @param host the host or host:port
     * @param user the username for login
     * @param password the password for login
     * @param database the database name
     * @return this connection
     */
    T connect(String host, String user, String password, String database);
    /**
     * Connects to the Database
     * @param host the host or host:port
     * @param user the username for login
     * @param password the password for login
     * @param database the database name
     * @param port the port on witch the Database lives
     * @return this connection
     */
    T connect(String host, String user, String password, String database, int port);
    /**
     * Connects to the Database
     * @param host the host or host:port
     * @param database the database name
     * @param port the port on witch the Database lives
     * @return this connection
     */
    T connect(String host, String database, int port);

}
