import lombok.AllArgsConstructor;
import lombok.Data;
import me.david.davidlib.database.connection.Connection;
import me.david.davidlib.database.connection.impl.MongoConnection;
import me.david.davidlib.database.connection.impl.MySQLConnection;
import me.david.davidlib.database.connection.sql.SQLDriverConnection;
import me.david.davidlib.database.repo.Filters;
import me.david.davidlib.database.repo.SQLExcecutor;
import me.david.davidlib.database.repo.TableExecutor;
import me.david.davidlib.database.table.*;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class Example {

    @Test
    public void exaple() {
        new MySQLConnection().connect("localhost", "database").makeDefault();

        TableExecutor<User, SQLDriverConnection> executor = new SQLExcecutor<>();

        User user = executor.selectFirst(Filters.and(Filters.eq("a", "ads"), Filters.eq("a", "a")));
        executor.save(user);

        String str = "A";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/EMP");

            PreparedStatement statement = connection.prepareStatement("SELECT * from user where a = ? AND b = ?");
            statement.setString(1, "a");
            statement.setString(2, "b");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    @Table(name = "user")
    @Data
    @AllArgsConstructor
    public class User {

        @Column(name = "a", type = ColumnType.TEXT, typeParameters = {255})
        @Primary
        @AutoIncrement
        @NotNull
        private String name;

        @Column
        private long id;

        @Column
        private String password;

    }

}
