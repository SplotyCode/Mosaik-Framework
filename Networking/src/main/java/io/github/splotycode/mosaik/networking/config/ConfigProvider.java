package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.listener.StringListenerHandler;

import java.util.Map;

public interface ConfigProvider {

    void set(String key, Object value);

    ConfigEntry getEntry(String key);
    Iterable<ConfigEntry> getEntries();
    Iterable<Map.Entry<String, Object>> getRawEntries();

    StringListenerHandler<ConfigChangeListener> handler();

    String getRawConfig();
    void setRawConfig(String config);

}
