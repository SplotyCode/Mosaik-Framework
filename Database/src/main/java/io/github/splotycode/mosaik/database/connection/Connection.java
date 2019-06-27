package io.github.splotycode.mosaik.database.connection;

import java.io.Closeable;

public interface Connection extends Closeable {

    /**
     * Checks is a connection is connected
     * @return true if the connection is connected or else false
     */
    boolean isConnected();

    /**
     * Disconnects this connection
     */
    void disconnect();

    @Override
    default void close() {
        disconnectIfNecessary();
    }

    void disconnectIfNecessary();

}
