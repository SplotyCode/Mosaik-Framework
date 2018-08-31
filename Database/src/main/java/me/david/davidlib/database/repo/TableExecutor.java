package me.david.davidlib.database.repo;

import me.david.davidlib.database.Database;
import me.david.davidlib.database.connection.Connection;

public interface TableExecutor<T> {

    void drop(Connection connection);
    void create(Connection connection);
    void createIfNotExists(Connection connection);

    void save(Connection connection, T entry);
    void save(Connection connection, T entry, String... fields);
    void save(Connection connection, T entry, Enum... fields);

    void delete(Connection connection, T entity);
    void deletePrimary(Connection connection, Object primary);
    void deleteFirst(Connection connection, Filters.Filter filter);
    void deleteAll(Connection connection, Filters.Filter filter);

    boolean exsits(Connection connection, Object primary);
    boolean exsits(Connection connection, Filters.Filter filter);

    Iterable<T> selectAll(Connection connection);
    Iterable<T> select(Connection connection, Object value);
    Iterable<T> select(Connection connection, Filters.Filter filter);
    T selectFirst(Connection connection, Object value);
    T selectFirst(Connection connection, Filters.Filter filter);

    long count(Connection connection);
    long count(Connection connection, Filters.Filter filter);

    void update(Connection connection, T entity);
    void update(Connection connection, T entity, Object object);
    void update(Connection connection, T entity, Filters.Filter filter);
    void update(Connection connection, T entity, String... fields);
    void update(Connection connection, T entity, Object object, String... fields);
    void update(Connection connection, T entity, Filters.Filter filter, String... fields);


    default void drop() {
        drop(Database.getInstance().getDefaultConnection());
    }

    default void create() {
        create(Database.getInstance().getDefaultConnection());
    }

    default void createIfNotExists() {
        createIfNotExists(Database.getInstance().getDefaultConnection());
    }

    default void save(T entry) {
        save(Database.getInstance().getDefaultConnection(), entry);
    }

    default void save(T entry, String... fields) {
        save(Database.getInstance().getDefaultConnection(), entry, fields);
    }

    default void save(T entry, Enum... fields) {
        save(Database.getInstance().getDefaultConnection(), entry, fields);
    }

    default void delete(T entity) {
        delete(Database.getInstance().getDefaultConnection(), entity);
    }

    default void deletePrimary(Object primary) {
        deletePrimary(Database.getInstance().getDefaultConnection(), primary);
    }

    default void deleteFirst(Filters.Filter filter) {
        deleteFirst(Database.getInstance().getDefaultConnection(), filter);
    }

    default void deleteAll(Filters.Filter filter) {
        deleteAll(Database.getInstance().getDefaultConnection(), filter);
    }

    default boolean exsits(Object primary) {
        return exsits(Database.getInstance().getDefaultConnection(), primary);
    }

    default boolean exsits(Filters.Filter filter) {
        return exsits(Database.getInstance().getDefaultConnection(), filter);
    }

    default Iterable<T> selectAll() {
        return selectAll(Database.getInstance().getDefaultConnection());
    }

    default Iterable<T> select(Object value) {
        return select(Database.getInstance().getDefaultConnection(), value);
    }

    default Iterable<T> select(Filters.Filter filter) {
        return select(Database.getInstance().getDefaultConnection(), filter);
    }

    default T selectFirst(Object value) {
        return selectFirst(Database.getInstance().getDefaultConnection(), value);
    }

    default T selectFirst(Filters.Filter filter) {
        return selectFirst(Database.getInstance().getDefaultConnection(), filter);
    }

    default long count() {
        return count(Database.getInstance().getDefaultConnection());
    }

    default long count(Filters.Filter filter) {
        return count(Database.getInstance().getDefaultConnection(), filter);
    }

    default void update(T entity) {
        update(Database.getInstance().getDefaultConnection(), entity);
    }

    default void update(T entity, Object object) {
        update(Database.getInstance().getDefaultConnection(), entity, object);
    }

    default void update(T entity, Filters.Filter filter) {
        update(Database.getInstance().getDefaultConnection(), entity, filter);
    }

    default void update(T entity, String... fields) {
        update(Database.getInstance().getDefaultConnection(), entity, fields);
    }

    default void update(T entity, Object object, String... fields) {
        update(Database.getInstance().getDefaultConnection(), entity, object, fields);
    }

    default void update(T entity, Filters.Filter filter, String... fields) {
        update(Database.getInstance().getDefaultConnection(), entity, filter, fields);
    }

}
