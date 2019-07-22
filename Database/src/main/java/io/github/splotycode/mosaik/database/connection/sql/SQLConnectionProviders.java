package io.github.splotycode.mosaik.database.connection.sql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLConnectionProviders {

    private static void checkDriver(String driver) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new NoDriverException("The Drivers are not included in Mosaik", e);
        }
    }

    public static JDBCConnectionProvider oracle() {
        checkDriver("oracle.jdbc.driver.OracleDriver");
        return new JDBCConnectionProvider("oracle:thin") {
            @Override
            public int getDefaultPort() {
                return 1521;
            }
        };
    }

    public static JDBCConnectionProvider mysql() {
        //checkDriver("com.mysql.jdbc.Driver");
        return new JDBCConnectionProvider("mysql");
    }

    public static JDBCConnectionProvider maria() {
        checkDriver("org.mariadb.jdbc.Driver");
        return new JDBCConnectionProvider("mariadb");
    }

    public static JDBCConnectionProvider sqlite() {
        checkDriver("org.sqlite.JDBC");
        return new JDBCConnectionProvider("sqlite");
    }

}
