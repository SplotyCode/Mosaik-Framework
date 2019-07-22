package io.github.splotycode.mosaik.database.repo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.github.splotycode.mosaik.database.connection.mongo.MongoConnectionProvider;
import io.github.splotycode.mosaik.database.table.ColumnNameResolver;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import org.bson.conversions.Bson;

public class MongoExecutor<T> extends AbstractExecutor<T, MongoConnectionProvider> {

    public MongoExecutor(Class<?> clazz) {
        super(clazz);
    }

    private MongoCollection getCollection(MongoConnectionProvider provider) {
        return provider.providerDatase().getCollection(name);
    }

    private boolean exists(MongoConnectionProvider provider) {
        for (MongoCursor<String> it = provider.providerDatase().listCollectionNames().iterator(); it.hasNext(); ) {
            if (it.next().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drop(MongoConnectionProvider connection) {
        getCollection(connection).drop();
    }

    @Override
    public void create(MongoConnectionProvider connection) {
        connection.providerDatase().createCollection(name);
    }

    @Override
    public void createIfNotExists(MongoConnectionProvider connection) {
        if (!exists(connection)) {
            create(connection);
        }
    }

    @Override
    public void save(MongoConnectionProvider connection, T entry) {

    }

    @Override
    public void save(MongoConnectionProvider connection, T entry, ColumnNameResolver... fields) {

    }

    @Override
    public void deleteFirst(MongoConnectionProvider connection, Filters.Filter filter) {
        getCollection(connection).deleteOne(buildFilter(filter));
    }

    @Override
    public void deleteAll(MongoConnectionProvider connection, Filters.Filter filter) {
        getCollection(connection).deleteMany(buildFilter(filter));
    }

    @Override
    public boolean exists(MongoConnectionProvider connection, Filters.Filter filter) {
        return getCollection(connection).find(buildFilter(filter)).iterator().hasNext();
    }

    @Override
    public Iterable<T> selectAll(MongoConnectionProvider connection, ColumnNameResolver... fields) {
        return null;
    }

    @Override
    public Iterable<T> selectAll(MongoConnectionProvider connection) {
        return null;
    }

    @Override
    public Iterable<T> select(MongoConnectionProvider connection, Filters.Filter filter, ColumnNameResolver... fields) {
        return null;
    }

    @Override
    public Iterable<T> select(MongoConnectionProvider connection, Filters.Filter filter) {
        return null;
    }

    @Override
    public T selectFirst(MongoConnectionProvider connection, Filters.Filter filter, ColumnNameResolver... fields) {
        return null;
    }

    @Override
    public T selectFirst(MongoConnectionProvider connection, Filters.Filter filter) {
        return null;
    }

    @Override
    public long count(MongoConnectionProvider connection) {
        return getCollection(connection).count();
    }

    @Override
    public long count(MongoConnectionProvider connection, Filters.Filter filter) {
        return getCollection(connection).count(buildFilter(filter));
    }

    private Bson[] convertArray(Filters.Filter[] filters) {
        Bson[] array = new Bson[filters.length];
        for (int i = 0; i < filters.length; i++) {
            array[i] = buildFilter(filters[i]);
        }
        return array;
    }

    private Bson buildFilter(Filters.Filter filter) {
        Filters.FilterType type = filter.type;
        if (filter instanceof Filters.ComplexFilter) {
            Bson[] filters = convertArray(((Filters.ComplexFilter) filter).getFilters());
            switch (filter.type) {
                case OR:
                    return com.mongodb.client.model.Filters.or(filters);
                case AND:
                    return com.mongodb.client.model.Filters.and(filters);
            }
        }
        Filters.ValueFilter valueFilter = (Filters.ValueFilter) filter;
        String value = TransformerManager.getInstance().transform(valueFilter.getObject(), String.class);

        switch (type) {
            case LESS:
                return com.mongodb.client.model.Filters.lt(valueFilter.field, value);
            case EQUAL:
                return com.mongodb.client.model.Filters.eq(valueFilter.field, value);
            case GREATER:
                return com.mongodb.client.model.Filters.gt(valueFilter.field, value);
            case GREATER_OR_EQUAL:
                return com.mongodb.client.model.Filters.gte(valueFilter.field, value);
            case LESS_OR_EQUAL:
                return com.mongodb.client.model.Filters.lte(valueFilter.field, value);
            case NOTEQUAL:
                return com.mongodb.client.model.Filters.ne(valueFilter.field, value);
        }
        throw new UnsupportedOperationException("Unsupported filter wit type " + type);
    }

    @Override
    public void update(MongoConnectionProvider connection, T entity, Filters.Filter filter) {

    }

    @Override
    public void update(MongoConnectionProvider connection, T entity, ColumnNameResolver... fields) {

    }

    @Override
    public void update(MongoConnectionProvider connection, T entity, Filters.Filter filter, ColumnNameResolver... fields) {

    }
}
