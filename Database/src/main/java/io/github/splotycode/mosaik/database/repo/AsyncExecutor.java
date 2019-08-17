package io.github.splotycode.mosaik.database.repo;

import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import io.github.splotycode.mosaik.database.table.ColumnNameResolver;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class AsyncExecutor<T, C extends ConnectionProvider> implements TableExecutor<T, C> {

    private TableExecutor<T, C> backing;
    private ExecutorService executor;

    public AsyncExecutor(TableExecutor<T, C> backing, ExecutorService executor) {
        this.backing = backing;
        this.executor = executor;
    }

    public void submit(Runnable runnable, Runnable callback) {
        executor.submit(() -> {
            runnable.run();
            if (callback != null) {
                callback.run();
            }
        });
    }

    @Override
    public Class<?> getRepoClass() {
        return backing.getRepoClass();
    }

    @Override
    public void drop(C connection) {
        drop(connection, null);
    }

    public void drop(C connection, Runnable callback) {
        submit(() -> backing.drop(connection), callback);
    }

    @Override
    public void create(C connection) {
        create(connection, null);
    }

    public void create(C connection, Runnable callback) {
        submit(() -> backing.create(connection), callback);
    }

    @Override
    public void createIfNotExists(C connection) {
        createIfNotExists(connection, null);
    }

    public void createIfNotExists(C connection, Runnable callback) {
        submit(() -> backing.createIfNotExists(connection), callback);
    }

    @Override
    public void save(C connection, T entry) {
        save(connection, entry, (Runnable) null);
    }

    public void save(C connection, T entry, Runnable callback) {
        submit(() -> backing.save(connection, entry), callback);
    }

    public void save(Runnable callback, C connection, T entry, ColumnNameResolver... fields) {
        submit(() -> backing.save(connection, entry, fields), callback);
    }

    @Override
    public void save(C connection, T entry, ColumnNameResolver... fields) {
        save(null, connection, entry, fields);
    }

    @Override
    public void deleteFirst(C connection, Filters.Filter filter) {
        deleteFirst(connection, filter, null);
    }

    public void deleteFirst(C connection, Filters.Filter filter, Runnable callback) {
        submit(() -> backing.deleteFirst(connection, filter), callback);
    }

    public void deleteAll(C connection, Filters.Filter filter, Runnable callback) {
        submit(() -> backing.deleteAll(connection, filter), callback);
    }

    @Override
    public void deleteAll(C connection, Filters.Filter filter) {
        deleteAll(connection, filter, null);
    }

    public void exists(C connection, Filters.Filter filter, Consumer<Boolean> callback) {
        executor.submit(() -> callback.accept(backing.exists(connection, filter)));
    }

    public void exists(Filters.Filter filter, Consumer<Boolean> callback) {
        executor.submit(() -> callback.accept(backing.exists(filter)));
    }

    @Override
    public boolean exists(C connection, Filters.Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void selectAll(Consumer<Iterable<T>> callback, C connection, ColumnNameResolver... fields) {
        executor.submit(() -> callback.accept(backing.selectAll(connection, fields)));
    }

    public void selectAll(Consumer<Iterable<T>> callback, ColumnNameResolver... fields) {
        executor.submit(() -> callback.accept(backing.selectAll(fields)));
    }

    public void selectAll(Consumer<Iterable<T>> callback, String... fields) {
        executor.submit(() -> callback.accept(backing.selectAll(fields)));
    }

    @Override
    public Iterable<T> selectAll(C connection, ColumnNameResolver... fields) {
        throw new UnsupportedOperationException();
    }

    public void selectAll(C connection, Consumer<Iterable<T>> callback) {
        executor.submit(() -> callback.accept(backing.selectAll(connection)));
    }

    public void selectAll(Consumer<Iterable<T>> callback) {
        executor.submit(() -> callback.accept(backing.selectAll()));
    }

    @Override
    public Iterable<T> selectAll(C connection) {
        throw new UnsupportedOperationException();
    }

    public void select(Consumer<Iterable<T>> callback, C connection, Filters.Filter filter, ColumnNameResolver... fields) {
        executor.submit(() -> callback.accept(backing.select(connection, filter, fields)));
    }

    public void select(Consumer<Iterable<T>> callback, Filters.Filter filter, String... fields) {
        executor.submit(() -> callback.accept(backing.select(filter, fields)));
    }

    public void selectFirst(Consumer<T> callback, Filters.Filter filter, String... fields) {
        executor.submit(() -> callback.accept(backing.selectFirst(filter, fields)));
    }

    @Override
    public Iterable<T> select(C connection, Filters.Filter filter, ColumnNameResolver... fields) {
        throw new UnsupportedOperationException();
    }

    public void select(C connection, Filters.Filter filter, Consumer<Iterable<T>> callback) {
        executor.submit(() -> callback.accept(backing.select(connection, filter)));
    }

    public void select(Filters.Filter filter, Consumer<Iterable<T>> callback) {
        executor.submit(() -> callback.accept(backing.select(filter)));
    }

    @Override
    public Iterable<T> select(C connection, Filters.Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void selectFirst(Consumer<T> callback, C connection, Filters.Filter filter, ColumnNameResolver... fields) {
        executor.submit(() -> callback.accept(backing.selectFirst(connection, filter, fields)));
    }

    public void selectFirst(Consumer<T> callback, Filters.Filter filter, ColumnNameResolver... fields) {
        executor.submit(() -> callback.accept(backing.selectFirst(filter, fields)));
    }

    @Override
    public T selectFirst(C connection, Filters.Filter filter, ColumnNameResolver... fields) {
        throw new UnsupportedOperationException();
    }

    public void selectFirst(C connection, Filters.Filter filter, Consumer<T> callback) {
        executor.submit(() -> callback.accept(backing.selectFirst(connection, filter)));
    }

    public void selectFirst(Filters.Filter filter, Consumer<T> callback) {
        executor.submit(() -> callback.accept(backing.selectFirst(filter)));
    }

    @Override
    public T selectFirst(C connection, Filters.Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void count(C connection, Consumer<Long> callback) {
        executor.submit(() -> callback.accept(backing.count(connection)));
    }

    public void count(Consumer<Long> callback) {
        executor.submit(() -> callback.accept(backing.count()));
    }

    @Override
    public long count(C connection) {
        throw new UnsupportedOperationException();
    }

    public void count(C connection, Filters.Filter filter, Consumer<Long> callback) {
        executor.submit(() -> callback.accept(backing.count(connection, filter)));
    }

    public void count(Filters.Filter filter, Consumer<Long> callback) {
        executor.submit(() -> callback.accept(backing.count(filter)));
    }

    @Override
    public long count(C connection, Filters.Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void update(C connection, T entity, Filters.Filter filter, Runnable callback) {
        submit(() -> backing.update(connection, entity, filter), callback);
    }

    public void update(T entity, Filters.Filter filter, Runnable callback) {
        submit(() -> backing.update(entity, filter), callback);
    }

    @Override
    public void update(C connection, T entity, Filters.Filter filter) {
        update(connection, entity, filter, (Runnable) null);
    }

    public void update(Runnable callback, C connection, T entity, ColumnNameResolver... fields) {
        submit(() -> backing.update(connection, entity, fields), callback);
    }

    public void update(Runnable callback, T entity, ColumnNameResolver... fields) {
        submit(() -> backing.update(entity, fields), callback);
    }

    @Override
    public void update(C connection, T entity, ColumnNameResolver... fields) {
        update(null, connection, entity, fields);
    }

    public void update(Runnable callback, C connection, T entity, Filters.Filter filter, ColumnNameResolver... fields) {
        submit(() -> backing.update(connection, entity, filter), callback);
    }

    public void update(Runnable callback, T entity, Filters.Filter filter, ColumnNameResolver... fields) {
        submit(() -> backing.update(entity, filter), callback);
    }

    @Override
    public void update(C connection, T entity, Filters.Filter filter, ColumnNameResolver... fields) {
        update(null, connection, entity, filter, fields);
    }

    public void save(Runnable callback, C connection, T entry, String... fields) {
        submit(() -> backing.save(connection, entry, fields), callback);
    }

    public void selectAll(Runnable callback, C connection, String... fields) {
        submit(() -> backing.selectAll(connection, fields), callback);
    }

    public void selectAll(Runnable callback, String... fields) {
        submit(() -> backing.selectAll(fields), callback);
    }

    public void select(Runnable callback, C connection, Filters.Filter filter, String... fields) {
        submit(() -> backing.select(connection, filter, fields), callback);
    }

    public void select(Runnable callback, Filters.Filter filter, String... fields) {
        submit(() -> backing.select(filter, fields), callback);
    }

    public void selectFirst(Consumer<T> callback, C connection, Filters.Filter filter, String... fields) {
        executor.submit(() -> callback.accept(backing.selectFirst(connection, filter, fields)));
    }

    public void update(Runnable callback, C connection, T entity, String... fields) {
        submit(() -> backing.update(connection, entity, fields), callback);
    }

    public void update(Runnable callback, T entity, String... fields) {
        submit(() -> backing.update(entity, fields), callback);
    }

    public void update(Runnable callback, C connection, T entity, Filters.Filter filter, String... fields) {
        submit(() -> backing.update(connection, entity, filter, fields), callback);
    }

    public void update(Runnable callback, T entity, Filters.Filter filter, String... fields) {
        submit(() -> backing.update(entity, filter, fields), callback);
    }

    public void drop(Runnable runnable) {
        submit(() -> backing.drop(), runnable);
    }

    public void create(Runnable runnable) {
        submit(() -> backing.create(), runnable);
    }

    public void createIfNotExists(Runnable runnable) {
        submit(() -> backing.createIfNotExists(), runnable);
    }

    public void save(T entry, Runnable runnable) {
        submit(() -> backing.save(entry), runnable);
    }

    public void save(Runnable runnable, T entry, String... fields) {
        submit(() -> backing.save(entry, fields), runnable);
    }

    public void save(Runnable runnable, T entry, ColumnNameResolver... fields) {
        submit(() -> backing.save(entry, fields), runnable);
    }

    public void deleteFirst(Filters.Filter filter, Runnable runnable) {
        submit(() -> backing.deleteFirst(filter), runnable);
    }

    public void deleteAll(Filters.Filter filter, Runnable runnable) {
        submit(() -> backing.deleteAll(filter), runnable);
    }

}
