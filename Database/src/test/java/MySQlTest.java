import lombok.Data;
import me.david.davidlib.database.table.*;
import org.junit.jupiter.api.Test;

public class MySQlTest {

    @Test
    public void testConnection() {
//        Connection<MySQLConnection> connection = new MySQLConnection().connect("localhost", "database");
  //      connection.makeDefault();

    //    TableExecutor<Person, SQLDriverConnection> repo = new SQLExcecutor<>();

    }

    @Table
    @Data
    public class Person {

        @Column
        private String firstName, lastName;

        @Column
        @AutoIncrement
        @NotNull
        private long id;

        @Column(type = ColumnType.TEXT)
        @Primary
        private String email;

    }
}
