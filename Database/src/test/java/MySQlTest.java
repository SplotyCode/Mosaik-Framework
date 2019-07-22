import io.github.splotycode.mosaik.database.connection.sql.JDBCConnectionProvider;
import io.github.splotycode.mosaik.database.connection.sql.SQLConnectionProviders;
import io.github.splotycode.mosaik.database.repo.Filters;
import io.github.splotycode.mosaik.database.repo.SQLExecutor;
import io.github.splotycode.mosaik.database.repo.TableExecutor;
import io.github.splotycode.mosaik.database.table.*;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.util.prettyprint.PrettyPrint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class MySQlTest {

    @Test
    void test() {
        if (StartUpInvoke.invokeTestSuite()) {
            SQLConnectionProviders.mysql().connect("localhost", "root", "1234", "test").makeDefault();

            TableExecutor<Person, JDBCConnectionProvider> repo = new SQLExecutor<>(Person.class);
            repo.drop();
            repo.createIfNotExists();
            repo.save(new Person("aa", "bbb", 2, "dasd@asd.ed"), Access.FIRST_NAME, Access.LAST_NAME);
            repo.save(new Person("aaa", "bbasb", 3, "dasd@asd.ed"));
            repo.save(new Person("aaa", "bbasb", 4, "dasd@asd.ed"));
            repo.save(new Person("aadsad", "bbbsdfd", 8, "dasd@asfdddd.ed"));
            System.out.println(new PrettyPrint(repo.selectAll()).prettyPrintType());
            System.out.println(new PrettyPrint(repo.selectFirst(
                    Filters.and(Filters.eq("firstName", "aaa"), Filters.gte("id", 4)))
            ).prettyPrint());
        }
    }

    enum  Access implements ColumnNameResolver {
        FIRST_NAME, LAST_NAME;

        @Override
        public String getColumnName() {
            return EnumUtil.toDisplayName(this, "", false);
        }

    }

    @Table
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {

        @Column(typeParameters = 30)
        private String firstName, lastName;

        @Column
        @AutoIncrement
        @Primary
        @NotNull
        private long id;

        @Column(type = ColumnType.TEXT, typeParameters = 20)
        private String email;

    }
}
