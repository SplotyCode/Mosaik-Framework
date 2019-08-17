package io.github.splotycode.mosaik.database;

import io.github.splotycode.mosaik.database.connection.ConnectionProvider;
import io.github.splotycode.mosaik.database.repo.TableExecutor;
import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.util.datafactory.DataKey;

import java.util.HashMap;

public interface DatabaseApplicationType extends ApplicationType {

    DataKey<ConnectionProvider<?, ?>> DEFAULT_CONNECTION = new DataKey<>("database.default_connection");
    DataKey<HashMap<Class, TableExecutor>> DEFAULT_EXECUTORS = new DataKey<>("database.default_executors");

    default void setDefault(ConnectionProvider<?, ?> connectionProvider) {
        putData(DEFAULT_CONNECTION, connectionProvider);
    }

    default void setDefault(TableExecutor executor) {
        getDefaultExecutors().put(executor.getRepoClass(), executor);
    }

    default void removeDefault(Class<?> clazz) {
        getDefaultExecutors().remove(clazz);
    }

    default void removeDefault(TableExecutor executor) {
        removeDefault(executor.getRepoClass());
    }

    default ConnectionProvider<?, ?> getDefaultConnection() {
        return getData(DEFAULT_CONNECTION);
    }

    default TableExecutor getDefaultExecutor(Class<?> clazz) {
        HashMap<Class, TableExecutor> map = getData(DEFAULT_EXECUTORS);
        if (map == null) {
            return null;
        }
        return map.get(clazz);
    }

    default HashMap<Class, TableExecutor> getDefaultExecutors() {
        HashMap<Class, TableExecutor> map = getData(DEFAULT_EXECUTORS);
        if (map == null) {
            map = new HashMap<>();
            putData(DEFAULT_EXECUTORS, map);
        }
        return map;
    }

}
