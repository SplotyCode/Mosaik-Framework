package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.connection.sql.JDBCConnection;
import io.github.splotycode.mosaik.database.connection.sql.JDBCConnectionProvider;
import io.github.splotycode.mosaik.database.table.ColumnNameResolver;
import io.github.splotycode.mosaik.database.table.ColumnType;
import io.github.splotycode.mosaik.database.table.FieldObject;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.EmptyIterable;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class SQLExecutor<T> extends AbstractExecutor<T, JDBCConnectionProvider> {

    private static Map<Filters.FilterType, String> filterTypes = new HashMap<>();

    public SQLExecutor(Class<?> clazz) {
        super(clazz);
    }

    static {
        filterTypes.put(Filters.FilterType.EQUAL, "=");
        filterTypes.put(Filters.FilterType.NOTEQUAL, "!=");
        filterTypes.put(Filters.FilterType.GREATER, ">");
        filterTypes.put(Filters.FilterType.LESS, "<");
        filterTypes.put(Filters.FilterType.LESS_OR_EQUAL, "<=");
        filterTypes.put(Filters.FilterType.GREATER_OR_EQUAL, ">=");
    }

    @Override
    public void drop(JDBCConnectionProvider connection) {
        exec(connection, new StringBuilder("DROP TABLE ").append(name), "DROP");
    }

    @Override
    public void create(JDBCConnectionProvider connection) {
        createTable(connection, false);
    }

    private void createTable(JDBCConnectionProvider connection, boolean ifNotExists) {
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
                StringUtil.removeEnd(builder, ", ", true);
                builder.append(") ");
            }
            if (object.isAutoIncrement()) builder.append(" AUTO_INCREMENT");
            if (object.isPrimary()) builder.append(" PRIMARY KEY");
            if (object.isNotNull()) builder.append(" NOT NULL");
            builder.append(", ");
        }
        if (fields.values().size() != 0) builder.setLength(builder.length() - 2);
        builder.append(")");
        exec(connection, builder, "Creating table");
    }

    private ColumnType getColumnType(FieldObject field) {
        ColumnType type = field.getColumn().type();
        if (type == ColumnType.NONE) {
            Class<?> clazz = field.getField().getType();
            if (ReflectionUtil.isAssignable(Integer.class, clazz)) {
                return ColumnType.INT;
            }
            if (ReflectionUtil.isAssignable(Boolean.class, clazz)) {
                return ColumnType.TINYINT;
            }
            if (ReflectionUtil.isAssignable(Long.class, clazz)) {
                return ColumnType.BIGINT;
            }
            if (String.class.isAssignableFrom(clazz)) {
                return ColumnType.VARCHAR;
            }
            if (ReflectionUtil.isAssignable(Short.class, clazz)) {
                return ColumnType.SMALLINT;
            }
        }
        return type;
    }

    @Override
    public void createIfNotExists(JDBCConnectionProvider connection) {
        createTable(connection, true);
    }

    @Override
    public void save(JDBCConnectionProvider connection, T entry) {
        save(connection, entry, allResolvers);
    }

    @Override
    public void save(JDBCConnectionProvider connection, T entry, ColumnNameResolver... fields) {
        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(name).append(" SET ");
        for (ColumnNameResolver fieldResolver : fields) {
            String field = fieldResolver.getColumnName();
            builder.append(field).append("='").append(getValue(field, entry)).append("', ");
        }
        StringUtil.removeEnd(builder, ", ");
        exec(connection, builder, "Saving Table");
    }

    /*@Override public void delete(JDBCConnectionProvider connection, T entity) {}
    @Override public void deletePrimary(JDBCConnectionProvider connection, Object primary) {}*/

    @Override
    public void deleteFirst(JDBCConnectionProvider connection, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append(name).append(generateWhere(filter));
        builder.append(" LIMIT 1;");
        exec(connection, builder, "Deleting rows");
    }

    private void exec(JDBCConnectionProvider provider, StringBuilder builder, String action) {
        String command = builder.toString();
        try (JDBCConnection connection = provider.provide()) {
            try (Statement statement = connection.getConnection().createStatement()) {
                statement.execute(command);
            }
        } catch (SQLException ex) {
            throw new RepoException("Error on " + action + ": " + ex.getMessage() + " Generated message '" + command + "'", ex);
        }
    }

    @Override
    public void deleteAll(JDBCConnectionProvider connection, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append(name).append(generateWhere(filter));
        exec(connection, builder, "Deleting rows");
    }

    @Override
    public boolean exists(JDBCConnectionProvider provider, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("SELECT null from ");
        builder.append(name).append(generateWhere(filter));
        try (JDBCConnection connection = provider.provide()) {
            try (Statement statement = connection.getConnection().createStatement()) {
                return statement.executeQuery(builder.toString()).next();
            }
        } catch (SQLException ex) {
            throw new RepoException("Error on exists check: " + ex.getMessage(), ex);
        }
    }

    @Override
    public long count(JDBCConnectionProvider provider) {
        try (JDBCConnection connection = provider.provide()) {
            try (PreparedStatement statement = connection.getConnection().prepareStatement("select count(*) from ?")) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    long count = 0;
                    while (result.next()) {
                        count = result.getLong(1);
                    }
                    return count;
                }
            }
        } catch (SQLException ex) {
            throw new RepoException("Error on counting rows: " + ex.getMessage(), ex);
        }
    }

    @Override
    public long count(JDBCConnectionProvider provider, Filters.Filter filter) {
        try (JDBCConnection connection = provider.provide()) {
            try (PreparedStatement statement = connection.getConnection().prepareStatement("select count(*) from ? ?")) {
                statement.setString(1, name);
                statement.setString(2, generateWhere(filter));
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return result.getLong(1);
                    }
                    return 0;
                }
            }
        } catch (SQLException ex) {
            throw new RepoException("Error on counting rows: " + ex.getMessage(), ex);
        }
    }

    private void update(JDBCConnectionProvider connection, T entity, Filters.Filter filter, Collection<FieldObject> fields) {
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append(table).append(" SET ");
        for (FieldObject field : fields) {
            builder.append(field.getName()).append(" = '").append(getValue(field, entity)).append("', ");
        }
        StringUtil.removeEnd(builder, ", ");
        builder.append(generateWhere(filter));
        exec(connection, builder, "Updating");
    }

    @Override
    public void update(JDBCConnectionProvider connection, T entity, Filters.Filter filter) {
        update(connection, entity, filter, fields.values());
    }

    @Override
    public void update(JDBCConnectionProvider connection, T entity, ColumnNameResolver... fields) {
        update(connection, entity, null, Arrays.stream(fields).map(resolver -> this.fields.get(resolver.getColumnName())).collect(Collectors.toList()));
    }

    @Override
    public void update(JDBCConnectionProvider connection, T entity, Filters.Filter filter, ColumnNameResolver... fields) {
        update(connection, entity, filter, Arrays.stream(fields).map(resolver -> this.fields.get(resolver.getColumnName())).collect(Collectors.toList()));
    }

    private String generateWhere(Filters.Filter filter) {
        if (filter == null) return "";
        return " where " + buildFilter(filter);
    }

    private String buildFilter(Filters.Filter filter) {
        Filters.FilterType type = filter.type;
        if (filter instanceof Filters.ComplexFilter) {
            return StringUtil.join(((Filters.ComplexFilter) filter).getFilters(), this::buildFilter, "  " + type.name() + " ");
        }
        String operator = filterTypes.get(type);
        Filters.ValueFilter valueFilter = (Filters.ValueFilter) filter;
        String value = TransformerManager.getInstance().transform(valueFilter.getObject(), String.class);
        return valueFilter.field + operator + "'" + value + "'";
    }

    @Override
    public Iterable<T> selectAll(JDBCConnectionProvider connection, ColumnNameResolver... fields) {
        return select(connection, null, fields);
    }

    @Override
    public Iterable<T> selectAll(JDBCConnectionProvider connection) {
        return select(connection, false, null, allResolvers);
    }

    @Override
    public Iterable<T> select(JDBCConnectionProvider connection, Filters.Filter filter, ColumnNameResolver... fields) {
        return select(connection, false, filter, fields);
    }

    @Override
    public Iterable<T> select(JDBCConnectionProvider connection, Filters.Filter filter) {
        return select(connection, false, filter);
    }

    private void appendColumn(StringBuilder builder, ColumnNameResolver... fields) {
        if (fields.length == 0) {
            builder.append('*');
        } else {
            builder.append(StringUtil.join(fields, ColumnNameResolver::getColumnName));
        }
    }

    private Iterable<T> select(JDBCConnectionProvider provider, boolean onlyOne, Filters.Filter filter, ColumnNameResolver... fields) {
        if (fields.length == 0) {
            fields = allResolvers;
        }

        StringBuilder builder = new StringBuilder("SELECT ");
        appendColumn(builder, fields);
        builder.append(" FROM ").append(name).append(generateWhere(filter));

        try (Statement statement = provider.provide().getConnection().createStatement()){
            try (ResultSet result = statement.executeQuery(builder.toString())) {
                boolean exists = result.next();
                if (exists) {
                    ArrayList<T> list = new ArrayList<>();
                    do {
                        T object = (T) clazz.newInstance();
                        for (ColumnNameResolver field : fields) {
                            String name = field.getColumnName();
                            setValue(name, result.getString(name), object);
                        }
                        list.add(object);
                    } while (result.next() && !onlyOne);
                    return list;
                }
            }
            return EmptyIterable.emptyIterable();
        } catch (SQLException ex) {
            throw new RepoException("Failed on executing select query", ex);
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RepoException("Failed to create new Object/setting values", ex);
        }
    }

    @Override
    public T selectFirst(JDBCConnectionProvider connection, Filters.Filter filter, ColumnNameResolver... fields) {
        Iterable<T> iterable = select(connection, true, filter, fields);
        if (iterable != null) {
            return iterable.iterator().next();
        }
        return null;
    }

    @Override
    public T selectFirst(JDBCConnectionProvider connection, Filters.Filter filter) {
        Iterable<T> iterable = select(connection, true, filter);
        if (iterable == null) return null;
        return iterable.iterator().next();
    }

}
