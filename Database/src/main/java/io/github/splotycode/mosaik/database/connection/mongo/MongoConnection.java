package io.github.splotycode.mosaik.database.connection.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.splotycode.mosaik.database.connection.Connection;
import lombok.Getter;

@Getter
public class MongoConnection implements Connection {

    private MongoClient client;
    MongoDatabase database;
    private boolean connected = true;

    public MongoConnection(MongoClient client, String database) {
        this(client, client.getDatabase(database));
    }

    public MongoConnection(MongoClient client, MongoDatabase database) {
        this.client = client;
        this.database = database;
    }

    @Override
    public void disconnect() {
        if (client != null)
            client.close();
        connected = false;
    }

    @Override
    public void disconnectIfNecessary() {}

}
