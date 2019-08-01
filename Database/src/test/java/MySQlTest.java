import io.github.splotycode.mosaik.database.connection.sql.JDBCConnectionProvider;
import io.github.splotycode.mosaik.database.connection.sql.SQLConnectionProviders;
import io.github.splotycode.mosaik.database.repo.Filters;
import io.github.splotycode.mosaik.database.repo.SQLExecutor;
import io.github.splotycode.mosaik.database.repo.TableExecutor;
import io.github.splotycode.mosaik.database.repo.UnsecuredSQLExecutor;
import io.github.splotycode.mosaik.database.table.*;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import io.github.splotycode.mosaik.util.prettyprint.PrettyPrint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MySQlTest {

    @Test
    void test() {
        if (StartUpInvoke.invokeTestSuite()) {
            SQLConnectionProviders.mysql().connect("localhost", "root", "1234", "test").makeDefault();

            testExecutor(new SQLExecutor<>(Person.class));
            testExecutor(new UnsecuredSQLExecutor<>(Person.class));
        }
    }

    private void testExecutor(TableExecutor<Person, JDBCConnectionProvider> repo) {
        repo.drop();
        repo.createIfNotExists();
        repo.save(new Person("aa", "bbb", 2, "dasd@asd.ed", Gender.MALE), Access.FIRST_NAME, Access.LAST_NAME);
        repo.save(new Person("aaa", "bbasb", 3, "dasd@asd.ed", Gender.MALE));
        repo.save(new Person("aaa", "bbasb", 4, "dasd@asd.ed", Gender.MALE));
        repo.save(new Person("aadsad", "bbbsdfd", 8, "dasd@asfdddd.ed", Gender.WOMAN));
        System.out.println(new PrettyPrint(repo.selectAll()).prettyPrintType());
        System.out.println(new PrettyPrint(repo.selectFirst(
                Filters.and(Filters.eq("firstName", "aaa"), Filters.gte("id", 4)))
        ).prettyPrint());
    }

    enum  Access implements ColumnNameResolver.EnumColumnName {

        FIRST_NAME, LAST_NAME;

    }

    @Table
    @Data
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class Person {

        @Column(typeParameters = 30)
        @NonNull
        private String firstName, lastName;

        @Column
        @AutoIncrement
        @Primary
        @NotNull
        @NonNull
        private long id;

        @Column(type = ColumnType.TEXT, typeParameters = 20)
        @NonNull
        private String email;

        @Column(type = ColumnType.TEXT, typeParameters = 800)
        @NonNull
        private Gender gender;

        @Column(type = ColumnType.TEXT, typeParameters = 400)
        private final Extra extra = new Extra();


        @Column(type = ColumnType.TEXT, typeParameters = 400)
        File file = new File("yyy");

    }

    public static class Extra {

        String extra = "2222";

        String extra5 = "2222";

        Extra2 extraaaa = new Extra2();

    }

    public static class Extra2 {

        String[] extras = new String[] {"aaa", "bbbb"};

        List<String> extras2 = Arrays.asList(extras);

        File file = new File("abc");

    }

    public enum Gender {

        MALE,
        WOMAN

    }
}
