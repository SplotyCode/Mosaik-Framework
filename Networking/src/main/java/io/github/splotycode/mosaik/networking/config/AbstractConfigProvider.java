package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.listener.StringListenerHandler;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigProvider implements ConfigProvider {

    protected Map<String, ConfigEntry> config = new HashMap<>();
    protected StringListenerHandler<ConfigChangeListener> handler = new StringListenerHandler<>();

    @Override
    public void set(String key, Object value) {
        ConfigEntry entry = config.computeIfAbsent(key, s -> new ConfigEntry(key, value));
        entry.setValue(value);
        handler.call(key, listener -> listener.onChange(key, entry));
    }

    @Override
    public ConfigEntry getEntry(String key) {
        return config.get(key);
    }

    @Override
    public Iterable<ConfigEntry> getEntries() {
        return config.values();
    }

    @Override
    public Iterable<Map.Entry<String, Object>> getRawEntries() {
        HashMap<String, Object> config = new HashMap<>(this.config.size(), 1);
        for (Map.Entry<String, ConfigEntry> value : this.config.entrySet()) {
            config.put(value.getKey(), value.getValue().getValue());
        }
        return config.entrySet();
    }

    @Override
    public StringListenerHandler<ConfigChangeListener> handler() {
        return handler;
    }


}
