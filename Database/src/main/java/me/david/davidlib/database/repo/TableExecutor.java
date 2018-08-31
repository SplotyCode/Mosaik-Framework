package me.david.davidlib.database.repo;

import me.david.davidlib.database.Database;
import me.david.davidlib.database.connection.Connection;

public interface TableExecutor<T, C extends Connection> {

    void drop(C connection);
    void create(C connection);
    void createIfNotExists(C connection);

    void save(C connection, T entry);
    void save(C connection, T entry, String... fields);
    void save(C connection, T entry, Enum... fields);

    void delete(C connection, T entity);
    void deletePrimary(C connection, Object primary);
    void deleteFirst(C connection, Filters.Filter filter);
    void deleteAll(C connection, Filters.Filter filter);

    boolean exists(C connection, Object primary);
    boolean exists(C connection, Filters.Filter filter);

    Iterable<T> selectAll(C connection);
    Iterable<T> select(C connection, Object value);
    Iterable<T> select(C connection, Filters.Filter filter);
    T selectFirst(C connection, Object value);
    T selectFirst(C connection, Filters.Filter filter);

    long count(C connection);
    long count(C connection, Filters.Filter filter);

    void update(C connection, T entity);
    void update(C connection, T entity, Object object);
    void update(C connection, T entity, Filters.Filter filter);
    void update(C connection, T entity, String... fields);
    void update(C connection, T entity, Object object, String... fields);
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

    default void save(T entry, Enum... fields) {
        save((C) Database.getInstance().getDefaultConnection(), entry, fields);
    }

    default void delete(T entity) {
        delete((C) Database.getInstance().getDefaultConnection(), entity);
    }

    default void deletePrimary(Object primary) {
        deletePrimary((C) Database.getInstance().getDefaultConnection(), primary);
    }

    default void deleteFirst(Filters.Filter filter) {
        deleteFirst((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default void deleteAll(Filters.Filter filter) {
        deleteAll((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default boolean exists(Object primary) {
        return exists((C) Database.getInstance().getDefaultConnection(), primary);
    }

    default boolean exists(Filters.Filter filter) {
        return exists((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default Iterable<T> selectAll() {
        return selectAll((C) Database.getInstance().getDefaultConnection());
    }

    default Iterable<T> select(Object value) {
        return select((C) Database.getInstance().getDefaultConnection(), value);
    }

    default Iterable<T> select(Filters.Filter filter) {
        return select((C) Database.getInstance().getDefaultConnection(), filter);
    }

    default T selectFirst(Object value) {
        return selectFirst((C) Database.getInstance().getDefaultConnection(), value);
    }

    default T selectFirst(Filters.Filter filter) {
        return selectFirst((C) Database.getInstance().getDefaultConnection(), filter);
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

    default void update(T entity, Object object) {
        update((C) Database.getInstance().getDefaultConnection(), entity, object);
    }

    default void update(T entity, Filters.Filter filter) {
        update((C) Database.getInstance().getDefaultConnection(), entity, filter);
    }

    default void update(T entity, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, fields);
    }

    default void update(T entity, Object object, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, object, fields);
    }

    default void update(T entity, Filters.Filter filter, String... fields) {
        update((C) Database.getInstance().getDefaultConnection(), entity, filter, fields);
    }

}
