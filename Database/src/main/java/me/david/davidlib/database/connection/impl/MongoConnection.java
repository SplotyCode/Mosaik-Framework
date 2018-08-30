package me.david.davidlib.database.connection.impl;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.david.davidlib.database.connection.AbstractConnection;

import java.util.Arrays;
import java.util.Collections;

public class MongoConnection extends AbstractConnection {

    @Getter private MongoClient client;
    @Getter private MongoDatabase database;
    @Getter private boolean connected;

    @Override
    public void disconnect() {
        if (client != null)
            client.close();
        connected = false;
    }

    @Override
    public AbstractConnection connect(String string) {
        disconnect();
        MongoClientURI uri = new MongoClientURI(string);
        client = new MongoClient(uri);
        database = client.getDatabase(uri.getDatabase());
        connected = true;
        return this;
    }

    @Override
    public AbstractConnection connect(String host, String user, String password, String database, int port) {
        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        client = new MongoClient(new ServerAddress(host, port), Collections.singletonList(credential), options);
        this.database = client.getDatabase(database);
        connected = true;
        return this;
    }

    @Override
    public AbstractConnection connect(String host, String database, int port) {
        client = new MongoClient(host, port);
        this.database = client.getDatabase(database);
        connected = true;
        return null;
    }

    public MongoConnection setDatabase(MongoDatabase database) {
        this.database = database;
        return this;
    }

    public MongoConnection setClient(MongoClient client) {
        this.client = client;
        return this;
    }

    @Override
    public int getDefaultPort() {
        return 27017;
    }

}
