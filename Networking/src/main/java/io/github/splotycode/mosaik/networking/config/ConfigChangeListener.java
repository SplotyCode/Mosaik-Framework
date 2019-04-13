package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.listener.Listener;

public interface ConfigChangeListener extends Listener {

    void onChange(String originalUpdate, ConfigEntry entry);

}
