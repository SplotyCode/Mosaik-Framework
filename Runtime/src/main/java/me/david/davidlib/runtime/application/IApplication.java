package me.david.davidlib.runtime.application;

import me.david.davidlib.util.datafactory.DataFactory;
import me.david.davidlib.util.datafactory.DataFactoryComponent;
import me.david.davidlib.util.datafactory.DataKey;
import me.david.davidlib.util.logger.Logger;

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
        return getDataFactory().getData(key);
    }

    DataFactory getConfig();
    default <T> T getConfig(DataKey<T> key) {
        return getConfig().getData(key);
    }

    default <T> void putConfig(DataKey<T> key, T value) {
        getConfig().putData(key, value);
    }

}
