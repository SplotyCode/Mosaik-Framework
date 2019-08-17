package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.Database;
import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import io.github.splotycode.mosaik.database.table.ColumnNameResolver;

import java.util.Arrays;

/**
 * Uses to execute commands on a repo
 * @param <T> the repo
 * @param <C> the connection
 */
public interface TableExecutor<T, C extends ConnectionProvider> {

    Class<?> getRepoClass();

    @SuppressWarnings("unchecked")
    default C getDefaultConnection() {
        return (C) Database.getInstance().getDefaultConnection();
    }

    /**
     * Deletes this repo
     * @param connection the connection that will be used
     */
    void drop(C connection);

    /**
     * Creates this repo
     * @param connection the connection that will be used
     */
    void create(C connection);

    /**
     * Deletes this repo if it does not exists
     * @param connection the connection that will be used
     */
    void createIfNotExists(C connection);

    /**
     * Saves all fields of a Repo entry
     * @param entry the entry that will be saved
     * @param connection the connection that will be used
     */
    void save(C connection, T entry);

    /**
     * Saves some fields of a Repo entry
     * @param entry the entry that will be saved
     * @param connection the connection that will be used
     * @param fields the fields you want so save
     */
    default void save(C connection, T entry, String... fields) {
        save(connection, entry, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }

    /**
     * Saves some fields of a Repo entry
     * @param entry the entry that will be saved
     * @param connection the connection that will be used
     * @param fields the fields you want so save
     */
    void save(C connection, T entry, ColumnNameResolver... fields);

    /*void delete(C connection, T entity);
    void deletePrimary(C connection, Object primary);*/

    /**
     * Deletes the first entry that apply's to the filer
     * @param connection the connection that will be used
     * @param filter the filter to find the right entry
     */
    void deleteFirst(C connection, Filters.Filter filter);

    /**
     * Deletes all entry's that apply's to the filer
     * @param connection the connection that will be used
     * @param filter the filter to find the right entry's
     */
    void deleteAll(C connection, Filters.Filter filter);

    //boolean exists(C connection, Object primary);

    /**
     * Checks if we can find a entry for that filer
     * @param connection the connection that will be used
     * @param filter the filter
     * @return true if there are at least one entry that matches with that filter
     */
    boolean exists(C connection, Filters.Filter filter);

    default Iterable<T> selectAll(C connection, String... fields) {
        return selectAll(connection, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }

    default Iterable<T> select(C connection, Filters.Filter filter, String... fields) {
        return select(connection, filter, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }

    default T selectFirst(C connection, Filters.Filter filter, String... fields) {
        return selectFirst(connection, filter, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }

    Iterable<T> selectAll(C connection, ColumnNameResolver... fields);
    Iterable<T> selectAll(C connection);
    Iterable<T> select(C connection, Filters.Filter filter, ColumnNameResolver... fields);
    Iterable<T> select(C connection, Filters.Filter filter);
    T selectFirst(C connection, Filters.Filter filter, ColumnNameResolver... fields);
    T selectFirst(C connection, Filters.Filter filter);

    long count(C connection);
    long count(C connection, Filters.Filter filter);

    //void update(C connection, T entity, Object object);
    void update(C connection, T entity, Filters.Filter filter);
    default void update(C connection, T entity, String... fields) {
        update(connection, entity, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }
    //void update(C connection, T entity, Object object, String... fields);

    default void update(C connection, T entity, Filters.Filter filter, String... fields) {
        update(connection, entity, filter, (ColumnNameResolver[]) Arrays.stream(fields).map(s -> (ColumnNameResolver) () -> s).toArray());
    }

    void update(C connection, T entity, ColumnNameResolver... fields);

    void update(C connection, T entity, Filters.Filter filter, ColumnNameResolver... fields);


    default void drop() {
        drop(getDefaultConnection());
    }

    default void create() {
        create(getDefaultConnection());
    }

    default void createIfNotExists() {
        createIfNotExists(getDefaultConnection());
    }

    default void save(T entry) {
        save(getDefaultConnection(), entry);
    }

    default void save(T entry, String... fields) {
        save(getDefaultConnection(), entry, fields);
    }

    default void save(T entry, ColumnNameResolver... fields) {
        save(getDefaultConnection(), entry, fields);
    }

    /*default void delete(T entity) {
        delete(getDefaultConnection(), entity);
    }

    default void deletePrimary(Object primary) {
        deletePrimary(getDefaultConnection(), primary);
    }*/

    default void deleteFirst(Filters.Filter filter) {
        deleteFirst(getDefaultConnection(), filter);
    }

    default void deleteAll(Filters.Filter filter) {
        deleteAll(getDefaultConnection(), filter);
    }

    /*default boolean exists(Object primary) {
        return exists(getDefaultConnection(), primary);
    }*/

    default boolean exists(Filters.Filter filter) {
        return exists(getDefaultConnection(), filter);
    }

    default Iterable<T> selectAll(String... fields) {
        return selectAll(getDefaultConnection(), fields);
    }

    /*default Iterable<T> select(Object value) {
        return select(getDefaultConnection(), value);
    }*/

    default Iterable<T> select(Filters.Filter filter, String... fields) {
        return select(getDefaultConnection(), filter, fields);
    }

    /*default T selectFirst(Object value) {
        return selectFirst(getDefaultConnection(), value);
    }*/

    default T selectFirst(Filters.Filter filter, String... fields) {
        return selectFirst(getDefaultConnection(), filter, fields);
    }

    default long count() {
        return count(getDefaultConnection());
    }

    default long count(Filters.Filter filter) {
        return count(getDefaultConnection(), filter);
    }

    /*default void update(T entity, Object object) {
        update(getDefaultConnection(), entity, object);
    }*/

    default void update(T entity, Filters.Filter filter) {
        update(getDefaultConnection(), entity, filter);
    }

    default void update(T entity, String... fields) {
        update(getDefaultConnection(), entity, fields);
    }

    /*default void update(T entity, Object object, String... fields) {
        update(getDefaultConnection(), entity, object, fields);
    }*/

    default void update(T entity, Filters.Filter filter, String... fields) {
        update(getDefaultConnection(), entity, filter, fields);
    }

    default void update(T entity, ColumnNameResolver... fields) {
        update(getDefaultConnection(), entity, fields);
    }

    default void update(T entity, Filters.Filter filter, ColumnNameResolver... fields) {
        update(getDefaultConnection(), entity, filter, fields);
    }

    default Iterable<T> selectAll(ColumnNameResolver... fields) {
        return selectAll(getDefaultConnection(), fields);
    }

    default Iterable<T> select(Filters.Filter filter, ColumnNameResolver... fields) {
        return select(getDefaultConnection(), filter, fields);
    }

    default T selectFirst(Filters.Filter filter, ColumnNameResolver... fields) {
        return selectFirst(getDefaultConnection(), filter, fields);
    }

    default Iterable<T> selectAll() {
        return selectAll(getDefaultConnection());
    }

    default Iterable<T> select(Filters.Filter filter) {
        return select(getDefaultConnection(), filter);
    }

    default T selectFirst(Filters.Filter filter) {
        return selectFirst(getDefaultConnection(), filter);
    }

}
