package io.github.splotycode.mosaik.database.connection.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import io.github.splotycode.mosaik.database.connection.config.DefaultConifgConnectionProvider;

import java.util.Collections;

public class MongoConnectionProvider extends DefaultConifgConnectionProvider<MongoConnectionProvider, MongoConnection> {

    private MongoConnection connection;

    @Override
    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Override
    public int getDefaultPort() {
        return 27017;
    }

    @Override
    public MongoConnection provide() {
        return connection;
    }

    public MongoClient providerClient() {
        return provide().getClient();
    }

    public MongoDatabase providerDatase() {
        return provide().getDatabase();
    }

    @Override
    public MongoConnectionProvider connect(String string) {
        disconnect();
        MongoClientURI uri = new MongoClientURI(string);
        connection = new MongoConnection(new MongoClient(uri), uri.getDatabase());
        return this;
    }

    @Override
    public MongoConnectionProvider connect(String host, String user, String password, String database, int port) {
        disconnect();
        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        ServerAddress address = new ServerAddress(host, port);
        connection = new MongoConnection(new MongoClient(address, Collections.singletonList(credential), options), database);
        return this;
    }

    @Override
    public MongoConnectionProvider connect(String host, String database, int port) {
        disconnect();
        connection = new MongoConnection(new MongoClient(host, port), database);
        return this;
    }
}
