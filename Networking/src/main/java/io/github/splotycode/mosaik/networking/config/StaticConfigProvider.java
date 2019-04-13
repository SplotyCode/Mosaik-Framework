package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.listener.StringListenerHandler;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StaticConfigProvider implements ConfigProvider {

    private File file;

    private Yaml yaml = new Yaml();
    private Map<String, ConfigEntry> config = new HashMap<>();

    private StringListenerHandler<ConfigChangeListener> handler = new StringListenerHandler<>();

    public StaticConfigProvider(File file) {
        this.file = file;
        if (file != null && file.exists()) {
            try {
                setRawConfig(FileUtil.loadFile(file), false);
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    @Override
    public void set(String key, Object value) {
        ConfigEntry entry = getEntry(key);
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

    @Override
    public String getRawConfig() {
        return yaml.dump(getRawEntries());
    }

    @Override
    public void setRawConfig(String rawConfig) {
        setRawConfig(rawConfig, true);
    }

    public void setRawConfig(String rawConfig, boolean update) {
        if (file != null && update) {
            try {
                FileUtil.writeToFile(file, rawConfig);
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
        HashMap<String, ConfigEntry> config = new HashMap<>();
        for (Map.Entry<String, Object> value : ((Map<String, Object>) yaml.load(rawConfig)).entrySet()) {
            ConfigEntry entry = new ConfigEntry(value.getKey(), value.getValue());
            handler.call(value.getKey(), listener -> listener.onChange(value.getKey(), entry));
            config.put(value.getKey(), entry);
        }
        this.config = config;
    }

}
