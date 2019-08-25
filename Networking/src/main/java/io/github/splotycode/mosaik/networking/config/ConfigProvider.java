package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.listener.StringListenerHandler;

import java.util.Map;

public interface ConfigProvider {

    void set(String key, Object value);

    ConfigEntry getEntry(String key);
    Iterable<ConfigEntry> getEntries();
    Iterable<Map.Entry<String, Object>> getRawEntries();

    default ConfigSection getSection(String key) {
        return new ConfigSection(this, key);
    }

    StringListenerHandler<ConfigChangeListener> handler();

    String getRawConfig();
    void setRawConfig(String config);

    default <T> T getValue(ConfigKey<T> key) {
        return getValue(key.getName(), key.getType(), key.getDef());
    }

    default <T> void setConfigValue(ConfigKey<T> key, T obj) {
        set(key.getName(), obj);
    }

    default <T> T getValue(String key, Class<T> clazz) {
        T result = getValue(key, clazz, null);
        if (result == null) throw new IllegalArgumentException("Config: " + key);
        return result;
    }

    default <T> T getValue(String key, Class<T> clazz, T def) {
        ConfigEntry entry = getEntry(key);
        if (entry == null) return def;
        T value = entry.getValue(clazz);
        if (value == null) return def;
        try {
            return entry.getValue(clazz);
        } catch (Throwable throwable) {
            return def;
        }
    }

}
