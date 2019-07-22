import io.github.splotycode.mosaik.database.connection.sql.JDBCConnectionProvider;
import io.github.splotycode.mosaik.database.connection.sql.SQLConnectionProviders;
import io.github.splotycode.mosaik.database.repo.Filters;
import io.github.splotycode.mosaik.database.repo.SQLExecutor;
import io.github.splotycode.mosaik.database.repo.TableExecutor;
import io.github.splotycode.mosaik.database.table.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Example {

    @Test
    @Disabled
    public void exaple() {
        SQLConnectionProviders.mysql().connect("localhost", "database").makeDefault();

        TableExecutor<User, JDBCConnectionProvider> executor = new SQLExecutor<>(User.class);

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
        private String name;

        @Column
        @Primary
        @AutoIncrement
        private long id;

        @Column
        private String password;

    }

}
