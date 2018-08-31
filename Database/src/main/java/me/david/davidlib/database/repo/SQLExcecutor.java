package me.david.davidlib.database.repo;

import me.david.davidlib.database.connection.sql.SQLDriverConnection;
import me.david.davidlib.database.table.FieldObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLExcecutor<T> extends AbstractExecutor<T, SQLDriverConnection> {


    @Override
    public void drop(SQLDriverConnection connection) {
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement("DROP TABLE ?");
            statement.setString(1, name);
            statement.execute();
            statement.close();
        } catch (SQLException ex) {
            throw new RepoException("Error on deleting Table: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void create(SQLDriverConnection connection) {
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(name).append(" (");
        for (FieldObject object : fields.values()) {
            builder.append(object.getName()).append(" ");
        }
    }

    @Override
    public void createIfNotExists(SQLDriverConnection connection) {

    }

    @Override
    public void save(SQLDriverConnection connection, T entry) {

    }

    @Override
    public void save(SQLDriverConnection connection, T entry, String... fields) {

    }

    @Override
    public void save(SQLDriverConnection connection, T entry, Enum... fields) {

    }

    @Override
    public void delete(SQLDriverConnection connection, T entity) {

    }

    @Override
    public void deletePrimary(SQLDriverConnection connection, Object primary) {

    }

    @Override
    public void deleteFirst(SQLDriverConnection connection, Filters.Filter filter) {

    }

    @Override
    public void deleteAll(SQLDriverConnection connection, Filters.Filter filter) {

    }

    @Override
    public boolean exists(SQLDriverConnection connection, Object primary) {
        return false;
    }

    @Override
    public boolean exists(SQLDriverConnection connection, Filters.Filter filter) {
        return false;
    }

    @Override
    public Iterable<T> selectAll(SQLDriverConnection connection) {
        return null;
    }

    @Override
    public Iterable<T> select(SQLDriverConnection connection, Object value) {
        return null;
    }

    @Override
    public Iterable<T> select(SQLDriverConnection connection, Filters.Filter filter) {
        return null;
    }

    @Override
    public T selectFirst(SQLDriverConnection connection, Object value) {
        return null;
    }

    @Override
    public T selectFirst(SQLDriverConnection connection, Filters.Filter filter) {
        return null;
    }

    @Override
    public long count(SQLDriverConnection connection) {
        return 0;
    }

    @Override
    public long count(SQLDriverConnection connection, Filters.Filter filter) {
        return 0;
    }

    @Override
    public void update(SQLDriverConnection connection, T entity) {

    }

    @Override
    public void update(SQLDriverConnection connection, T entity, Object object) {

    }

    @Override
    public void update(SQLDriverConnection connection, T entity, Filters.Filter filter) {

    }

    @Override
    public void update(SQLDriverConnection connection, T entity, String... fields) {

    }

    @Override
    public void update(SQLDriverConnection connection, T entity, Object object, String... fields) {

    }

    @Override
    public void update(SQLDriverConnection connection, T entity, Filters.Filter filter, String... fields) {

    }

}
