import lombok.Data;
import me.david.davidlib.database.connection.Connection;
import me.david.davidlib.database.connection.impl.MySQLConnection;
import me.david.davidlib.database.repo.TableExecutor;
import me.david.davidlib.database.table.Field;
import me.david.davidlib.database.table.Primary;
import me.david.davidlib.database.table.RowType;
import me.david.davidlib.database.table.Table;
import org.junit.jupiter.api.Test;

public class MySQlTest {

    @Test
    public void testConnection() {
        Connection<MySQLConnection> connection = new MySQLConnection().connect("localhost", "database");
        connection.makeDefault();

        TableExecutor<Person> repo = null;

        repo.delete();

    }

    @Table
    @Data
    public class Person {

        @Field
        private String firstName, lastName;

        @Field(type = RowType.TEXT)
        @Primary
        private String email;

    }
}
