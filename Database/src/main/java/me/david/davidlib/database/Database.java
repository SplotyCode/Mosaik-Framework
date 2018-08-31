package me.david.davidlib.database;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.database.connection.Connection;

public class Database {

    @Getter private static Database instance = new Database();

    private Database() {}

    @Getter @Setter private Connection<?> defaultConnection;



}
