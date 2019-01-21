package io.github.splotycode.mosaik.database;

import io.github.splotycode.mosaik.database.connection.Connection;
import lombok.Getter;
import lombok.Setter;

public class Database {

    @Getter private static Database instance = new Database();

    private Database() {}

    @Getter @Setter private Connection<?> defaultConnection;



}
