package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.io.FileUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StaticConfigProvider extends AbstractConfigProvider {

    private File file;

    private Yaml yaml = new Yaml();

    public StaticConfigProvider(File file) {
        this.file = file;
        if (file != null && file.exists()) {
            setRawConfig(FileUtil.loadFile(file), false);
        }
    }

    public StaticConfigProvider(String content) {
        setRawConfig(content, false);
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
            FileUtil.writeToFile(file, rawConfig);
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
