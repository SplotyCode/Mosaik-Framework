package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.reflection.GenereticGuesser;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConfigKey<T> {

    public ConfigKey(String name) {
        this(name, (Class<T>) null);
        type = (Class<T>) GenereticGuesser.find(this, ConfigKey.class, "T");
    }

    public ConfigKey(String name, T def) {
        this(name);
        this.def = def;
    }

    public ConfigKey(String name, Class<T> type) {
        this(name, type, null);
    }

    private String name;
    private Class<T> type;
    private T def;

}
