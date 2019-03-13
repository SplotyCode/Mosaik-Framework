package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataFactoryComponent;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.logger.Logger;

import java.util.Collection;

public interface IApplication extends DataFactoryComponent {

    String getName();

    Logger getLogger();

    ApplicationState getState();
    void setState(ApplicationState state);

    Application getApplication();

    IShutdownManager getLocalShutdownManager();

    Collection<Class<ApplicationType>> getApplicationTypes();

    default <T> T getData(DataKey<T> key) {
        return getDataFactory().getDataDefault(key);
    }

    default <T> void putData(DataKey<T> key, T value) {
        getDataFactory().putData(key, value);
    }

    DataFactory getConfig();
    default <T> T getConfig(DataKey<T> key) {
        return getConfig().getDataDefault(key);
    }

    default <T> void putConfig(DataKey<T> key, T value) {
        getConfig().putData(key, value);
    }

}
