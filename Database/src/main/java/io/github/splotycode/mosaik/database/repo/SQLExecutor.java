package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.connection.sql.JDBCConnection;
import io.github.splotycode.mosaik.database.connection.sql.JDBCConnectionProvider;
import io.github.splotycode.mosaik.database.table.ColumnNameResolver;
import io.github.splotycode.mosaik.database.table.FieldObject;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.EmptyIterable;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

//TODO streaming support
public class SQLExecutor<T> extends UnsecuredSQLExecutor<T> {

    private static final String[] QUESTION_MARK;

    static {
        QUESTION_MARK = new String[36];
        QUESTION_MARK[0] = "";
        for (int i = 1; i < 36; i++) {
            QUESTION_MARK[i] = StringUtil.removeLast(StringUtil.repeat("?, ", i), 2);
        }
    }

    public SQLExecutor(Class<?> clazz) {
        super(clazz);
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

    @Override
    protected void update(JDBCConnectionProvider connection, T entity, Filters.Filter filter, Collection<FieldObject> fields) {
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
                return statement;
            }
            return connection.prepareStatement(builder.toString() + suffix);
        } catch (SQLException ex) {
            throw new RepoException("Errored while building filter generated: " + builder.toString() + suffix, ex);
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
            builder.append(valueFilter.field).append(operator).append("?");
        }
    }

    @Override
    protected Iterable<T> select(JDBCConnectionProvider provider, boolean onlyOne, Filters.Filter filter, ColumnNameResolver... fields) {
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

}
