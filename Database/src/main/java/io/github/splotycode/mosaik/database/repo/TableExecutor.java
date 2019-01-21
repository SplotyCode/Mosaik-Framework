package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.table.ColumnNameResolver;
import io.github.splotycode.mosaik.database.Database;
import io.github.splotycode.mosaik.database.connection.Connection;

/**
 * Uses to execute commands on a repo
 * @param <T> the repo
 * @param <C> the connection
 */
public interface TableExecutor<T, C extends Connection> {

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
    void save(C connection, T entry, String... fields);

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

    Iterable<T> selectAll(C connection, String... fields);
    Iterable<T> select(C connection, Filters.Filter filter, String... fields);
    T selectFirst(C connection, Filters.Filter filter, String... fields);

    long count(C connection);
    long count(C connection, Filters.Filter filter);

    //void update(C connection, T entity, Object object);
    void update(C connection, T entity, Filters.Filter filter);
    void update(C connection, T entity, String... fields);
    //void update(C connection, T entity, Object object, String... fields);
    void update(C connection, T entity, Filters.Filter filter, String... fields);


    default void drop() {
        drop((C) Database.getInstance().getDefaultConnection());
    }

    default void create() {
        create((C) Database.getInstance().getDefaultConnection());
    }

    default void createIfNotExists() {
        createIfNotExists((C) Database.getInstance().getDefaultConnection());
    }

    default void save(T entry) {
        save((C) Database.getInstance().getDefaultConnection(), entry);
    }

    default void save(T entry, String... fields) {
        save((C) Database.getInstance().getDefaultConnection(), entry, fields);
    }

    default void save(T entry, ColumnNameResolver... fields) {
        save((C) Database.getInstance().getDefaultConnection(), entry, fields);
    }

    /*default void delete(T entity) {
        delete((C) Database.getInstance().getDefaultConnection(), entity);
    }

    default void deletePrimary(Object primary) {
        deletePrimary((C) Database.getInstance().getDefaultConnection(), primary);
    }*/

    default void deleteFirst(Filters.Filter filter) {
        deleteFirst((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default void deleteAll(Filters.Filter filter) {
        deleteAll((C) Database.getInstance().getDefaultConnection(), filter);
    }

    /*default boolean exists(Object primary) {
        return exists((C) Database.getInstance().getDefaultConnection(), primary);
    }*/

    default boolean exists(Filters.Filter filter) {
        return exists((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default Iterable<T> selectAll(String... fields) {
        return selectAll((C) Database.getInstance().getDefaultConnection(), fields);
    }

    /*default Iterable<T> select(Object value) {
        return select((C) Database.getInstance().getDefaultConnection(), value);
    }*/

    default Iterable<T> select(Filters.Filter filter, String... fields) {
        return select((C) Database.getInstance().getDefaultConnection(), filter, fields);
    }

    /*default T selectFirst(Object value) {
        return selectFirst((C) Database.getInstance().getDefaultConnection(), value);
    }*/

    default T selectFirst(Filters.Filter filter, String... fields) {
        return selectFirst((C) Database.getInstance().getDefaultConnection(), filter, fields);
    }

    default long count() {
        return count((C) Database.getInstance().getDefaultConnection());
    }

    default long count(Filters.Filter filter) {
        return count((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default void update(T entity) {
        update((C) Database.getInstance().getDefaultConnection(), entity);
    }

    /*default void update(T entity, Object object) {
        update((C) Database.getInstance().getDefaultConnection(), entity, object);
    }*/

    default void update(T entity, Filters.Filter filter) {
        update((C) Database.getInstance().getDefaultConnection(), entity, filter);
    }

    default void update(T entity, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, fields);
    }

    /*default void update(T entity, Object object, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, object, fields);
    }*/

    default void update(T entity, Filters.Filter filter, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, filter, fields);
    }

}
