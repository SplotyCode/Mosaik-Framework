package me.david.davidlib.database.repo;

import me.david.davidlib.database.connection.sql.SQLDriverConnection;
import me.david.davidlib.database.table.ColumnNameResolver;
import me.david.davidlib.database.table.ColumnType;
import me.david.davidlib.database.table.FieldObject;
import me.david.davidlib.utils.reflection.ReflectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExececutor<T> extends AbstractExecutor<T, SQLDriverConnection> {

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
        createTable(connection, false);
    }

    private void createTable(SQLDriverConnection connection, boolean ifNotExists) {
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        if (ifNotExists) builder.append("IF NOT EXISTS ");
        builder.append(name).append(" (");
        for (FieldObject object : fields.values()) {
            builder.append(object.getName()).append(" ");
            builder.append(getColumnType(object).name());

            if (object.getColumn().typeParameters().length != 0) {
                builder.append("(");
                for (int parameter : object.getColumn().typeParameters()) {
                    builder.append(parameter).append(", ");
                }
                builder.setLength(builder.length() - 2);
                builder.append(") ");
            }
            if (object.isAutoIncrement()) builder.append("AUTO_INCREMENT");
            if (object.isPrimary()) builder.append("PRIMARY KEY");
            if (object.isNotNull()) builder.append("NOT NULL");
            builder.append(", ");
        }
        if (fields.values().size() != 0) builder.setLength(builder.length() - 2);
        builder.append(")");
        try {
            Statement statement = connection.getConnection().createStatement();
            statement.execute(builder.toString());
            statement.close();
        } catch (SQLException ex) {
            throw new RepoException("Error on creating Table: " + ex.getMessage(), ex);
        }
    }

    private ColumnType getColumnType(FieldObject field) {
        ColumnType type = field.getColumn().type();
        if (type == ColumnType.NONE) {
            Class<?> clazz = field.getField().getType();
            if (ReflectionUtil.isAssignable(Integer.class, clazz)) {
                return ColumnType.INT;
            }
            if (String.class.isAssignableFrom(clazz)) {
                return ColumnType.VARCHAR;
            }
        }
        return type;
    }

    @Override
    public void createIfNotExists(SQLDriverConnection connection) {
        createTable(connection, true);
    }

    @Override
    public void save(SQLDriverConnection connection, T entry) {
        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(name).append(" (");
        for (FieldObject field : fields.values()) {
            try {
                builder.append(field.getField().get(entry).toString()).append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fields.size() != 0) builder.setLength(builder.length() - 2);
        builder.append(")");
        try {
            Statement statement = connection.getConnection().createStatement();
            statement.execute(builder.toString());
            statement.close();
        } catch (SQLException ex) {
            throw new RepoException("Error on saving Table: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void save(SQLDriverConnection connection, T entry, String... fields) {
        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(name).append(" (");
        for (String field : fields) {
            try {
                builder.append(this.fields.get(field).getField().get(entry).toString()).append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fields.length != 0) builder.setLength(builder.length() - 2);
        builder.append(")");
        try {
            Statement statement = connection.getConnection().createStatement();
            statement.execute(builder.toString());
            statement.close();
        } catch (SQLException ex) {
            throw new RepoException("Error on saving Table: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void save(SQLDriverConnection connection, T entry, ColumnNameResolver... fields) {
        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(name).append(" (");
        for (ColumnNameResolver fieldResolver : fields) {
            String field = fieldResolver.getColumnName();
            try {
                builder.append(this.fields.get(field).getField().get(entry).toString()).append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fields.length != 0) builder.setLength(builder.length() - 2);
        builder.append(")");
        try {
            Statement statement = connection.getConnection().createStatement();
            statement.execute(builder.toString());
            statement.close();
        } catch (SQLException ex) {
            throw new RepoException("Error on saving Table: " + ex.getMessage(), ex);
        }
    }

    /*@Override public void delete(SQLDriverConnection connection, T entity) {}
    @Override public void deletePrimary(SQLDriverConnection connection, Object primary) {}*/

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
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement("select count(*) from ?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            long count = 0;
            while (result.next()) {
                count = result.getLong(1);
            }
            result.close();
            statement.close();
            return count;
        } catch (SQLException ex) {
            throw new RepoException("Error on counting rows: " + ex.getMessage(), ex);
        }
    }

    @Override
    public long count(SQLDriverConnection connection, Filters.Filter filter) {
        try {
            PreparedStatement statement = connection.getConnection().prepareStatement("select count(*) from ? where ?");
            statement.setString(1, name);
            statement.setString(2, generateWhere(filter));
            ResultSet result = statement.executeQuery();
            long count = 0;
            while (result.next()) {
                count = result.getLong(1);
            }
            result.close();
            statement.close();
            return count;
        } catch (SQLException ex) {
            throw new RepoException("Error on counting rows: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(SQLDriverConnection connection, T entity) {

    }

    private String generateWhere(Filters.Filter filter) {
        return "";
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
