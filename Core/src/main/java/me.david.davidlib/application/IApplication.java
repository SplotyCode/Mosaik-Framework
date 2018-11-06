package me.david.davidlib.application;

import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.datafactory.DataKey;

import java.util.Collection;

public interface IApplication {

    String getName();

    Application getApplication();


    IShutdownManager getLocalShutdownManager();
    IShutdownManager getGlobalShutdownManager();

    Collection<Class<ApplicationType>> getApplicationTypes();

    DataFactory getDataFactory();
    default <T> T getData(DataKey<T> key) {
        return getDataFactory().getData(key);
    }

    DataFactory getConfig();
    default <T> T getConfig(DataKey<T> key) {
        return getConfig().getData(key);
    }

}
