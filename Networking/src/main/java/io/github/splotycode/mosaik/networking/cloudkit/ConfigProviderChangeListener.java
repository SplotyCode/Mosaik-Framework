package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface ConfigProviderChangeListener extends Listener {

    void newConfigProvider(ConfigProvider old, ConfigProvider newProvider);

}
