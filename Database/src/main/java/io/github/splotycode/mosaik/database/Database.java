package io.github.splotycode.mosaik.database;

import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import lombok.Getter;
import lombok.Setter;

public class Database {

    @Getter private static Database instance = new Database();

    private Database() {}

    @Getter @Setter private ConnectionProvider<?, ?> defaultConnection;

}
