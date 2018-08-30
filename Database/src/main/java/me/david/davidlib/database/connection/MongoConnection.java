package me.david.davidlib.database.connection;

import com.mongodb.MongoClient;
import com.mongodb.event.*;
import lombok.Getter;

public class MongoConnection extends AbstractConnection {

    private MongoClient client;
    @Getter private boolean connected;

    @Override
    public void disconnect() {

    }

    @Override
    public AbstractConnection connect(String string) {
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
        return 0;
    }

}
