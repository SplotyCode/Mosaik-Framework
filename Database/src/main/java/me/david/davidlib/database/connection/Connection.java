package me.david.davidlib.database.connection;

import me.david.davidlib.database.Database;

/**
 * Symbolises a Database Connection
 * @param <T> this type
 */
public interface Connection<T extends Connection> {

    /**
     * Checks is a connection is connected
     * @return true if the connection is connected or else false
     */
    boolean isConnected();

    /**
     * Disconnects this connection
     */
    void disconnect();

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

    /**
     * Gets the default port for this connection port
     * @return the port
     */
    int getDefaultPort();

    /**
     * Makes this the default connection
     */
    default void makeDefault() {
        Database.getInstance().setDefaultConnection(this);
    }

}
