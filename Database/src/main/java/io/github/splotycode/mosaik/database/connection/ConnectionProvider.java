package io.github.splotycode.mosaik.database.connection;

import io.github.splotycode.mosaik.database.Database;

/**
 * Symbolises a Database Connection
 * @param <T> this type
 */
public interface ConnectionProvider<T extends ConnectionProvider, C extends Connection> {

    void disconnect();

    C provide();

    /**
     * Makes this the default connection
     */
    default void makeDefault() {
        Database.getInstance().setDefaultConnection(this);
    }

}
