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

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

//TODO streaming support
public class SQLExecutor<T> extends AbstractExecutor<T, JDBCConnectionProvider> {

    private static final Map<Filters.FilterType, String> FILTER_TYPES;
    private static final String[] QUESTION_MARK;

    static {
        QUESTION_MARK = new String[36];
        QUESTION_MARK[0] = "";
        for (int i = 1; i < 36; i++) {
            QUESTION_MARK[i] = StringUtil.removeLast(StringUtil.repeat("? ", i), 1);
        }
    }

    public SQLExecutor(Class<?> clazz) {
        super(clazz);
    }

    static {
        FILTER_TYPES = new HashMap<>(6, 1);
        FILTER_TYPES.put(Filters.FilterType.EQUAL, "=");
        FILTER_TYPES.put(Filters.FilterType.NOTEQUAL, "!=");
        FILTER_TYPES.put(Filters.FilterType.GREATER, ">");
        FILTER_TYPES.put(Filters.FilterType.LESS, "<");
        FILTER_TYPES.put(Filters.FilterType.LESS_OR_EQUAL, "<=");
        FILTER_TYPES.put(Filters.FilterType.GREATER_OR_EQUAL, ">=");
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
            if (ReflectionUtil.isAssignable(Double.class, clazz)) {
                return ColumnType.DOUBLE; //todo double check
            }
            if (ReflectionUtil.isAssignable(Float.class, clazz)) {
                return ColumnType.FLOAT; //todo reaL?
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
        builder.append(name).append(" (");
        StringUtil.join(builder, fields, ColumnNameResolver::getColumnName, ", ", false);
        builder.append(") VALUES (").append(QUESTION_MARK[fields.length]).append(")");

        try (JDBCConnection conn = connection.provide()){
            try (PreparedStatement statement = conn.getConnection().prepareStatement(builder.toString())){
                for (int i = 0; i < fields.length; i++) {
                    statement.setString(i + 1, getValue(fields[i].getColumnName(), entry));
                }
                exec(statement, "Saving Table");
            }
        } catch (SQLException ex) {
            throw new RepoException("Failed to save table", ex);
        }
    }

    /*@Override public void delete(JDBCConnectionProvider connection, T entity) {}
    @Override public void deletePrimary(JDBCConnectionProvider connection, Object primary) {}*/

    @Override
    public void deleteFirst(JDBCConnectionProvider connection, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("DELETE FROM ").append(name);
        executeWithWhere(connection, builder, " LIMIT 1;", filter, "Deleting rows");
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

    private void exec(PreparedStatement statement, String action) {
        try {
            statement.execute();
        } catch (SQLException ex) {
            throw new RepoException("Error on " + action + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAll(JDBCConnectionProvider connection, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("DELETE FROM ").append(name);
        executeWithWhere(connection, builder, "", filter, "Deleting rows");
    }

    @Override
    public boolean exists(JDBCConnectionProvider provider, Filters.Filter filter) {
        StringBuilder builder = new StringBuilder("SELECT null from ").append(name);
        try (JDBCConnection connection = provider.provide()) {
            try (PreparedStatement statement = generateWhere(builder, "", connection.getConnection(), filter)) {
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
        StringBuilder builder = new StringBuilder("select count(*) from ").append(name);
        try (JDBCConnection connection = provider.provide()) {
            try (PreparedStatement statement = generateWhere(builder, "", connection.getConnection(), filter)) {
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
        StringBuilder builder = new StringBuilder("UPDATE ")
                .append(table)
                .append(" SET ");
        StringUtil.join(builder, fields, (childBuilder, field) -> childBuilder.append(field.getName()).append(" = '?'"), ", ", false);
        try (JDBCConnection conn = connection.provide()) {
            try (PreparedStatement statement = generateWhere(builder, "", conn.getConnection(), filter, fields.size())) {
                int i = 1;
                for (FieldObject field : fields) {
                    statement.setString(i, getValue(field, entity));
                    i++;
                }
                exec(statement, "Updating");
            } catch (SQLException ex) {
                throw new RepoException("Failed to build prepared statement to update", ex);
            }
        }
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

    private void executeWithWhere(JDBCConnectionProvider provider, StringBuilder builder, String suffix, Filters.Filter filter, String action) {
        try (JDBCConnection connection = provider.provide()) {
            generateWhere(builder, suffix, connection.getConnection(), filter).execute();
        } catch (SQLException ex) {
            throw new RepoException("Error on " + action + ": " + ex.getMessage(), ex);
        }
    }

    private PreparedStatement generateWhere(StringBuilder builder, String suffix, Connection connection, Filters.Filter filter) {
        return generateWhere(builder, suffix, connection, filter, 0);
    }

    private PreparedStatement generateWhere(StringBuilder builder, String suffix, Connection connection, Filters.Filter filter, int offset) {
        try {
            if (filter != null) {
                ArrayList<String> placeholders = new ArrayList<>();
                builder.append(" where ");
                buildFilter(builder, filter, placeholders);
                builder.append(suffix);
                PreparedStatement statement = connection.prepareStatement(builder.toString());
                for (int i = 0; i < placeholders.size(); i++) {
                    statement.setString(i + 1 + offset, placeholders.get(i));
                }
            }
            return connection.prepareStatement(builder.toString() + suffix);
        } catch (SQLException ex) {
            throw new RepoException("Errored while building filter");
        }
    }

    private void buildFilter(StringBuilder builder, Filters.Filter filter, ArrayList<String> placeholders) {
        Filters.FilterType type = filter.type;
        if (filter instanceof Filters.ComplexFilter) {
            StringUtil.join(builder, ((Filters.ComplexFilter) filter).getFilters(),
                    (childBuilder, childFilter) -> buildFilter(childBuilder, childFilter, placeholders),
                    "  " + type.name() + " ", false);
        } else {
            String operator = FILTER_TYPES.get(type);
            Filters.ValueFilter valueFilter = (Filters.ValueFilter) filter;
            placeholders.add(TransformerManager.getInstance().transform(valueFilter.getObject(), String.class));
            builder.append(valueFilter.field).append(operator).append("'?'");
        }
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
        builder.append(" FROM ").append(name);

        try (JDBCConnection conn = provider.provide()) {
            try (PreparedStatement statement = generateWhere(builder, "", conn.getConnection(), filter)) {
                try (ResultSet result = statement.executeQuery()) {
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
