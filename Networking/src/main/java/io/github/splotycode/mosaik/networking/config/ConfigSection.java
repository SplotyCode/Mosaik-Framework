package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.util.collection.FilteredIterator;
import io.github.splotycode.mosaik.util.listener.StringListenerHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter
@Setter
public class ConfigSection implements ConfigProvider {

    private ConfigProvider base;
    private String prefix;

    @Override
    public void set(String key, Object value) {
        base.set(prefix + key, value);
    }

    @Override
    public ConfigEntry getEntry(String key) {
        return base.getEntry(prefix + key);
    }

    @Override
    public ConfigSection getSection(String key) {
        return base.getSection(prefix + key);
    }

    @Override
    public Iterable<ConfigEntry> getEntries() {
        return () -> new FilteredIterator<>(base.getEntries().iterator(), configEntry -> configEntry.getKey().startsWith(prefix));
    }

    @Override
    public Iterable<Map.Entry<String, Object>> getRawEntries() {
        return () -> new FilteredIterator<>(base.getRawEntries().iterator(), configEntry -> configEntry.getKey().startsWith(prefix));
    }

    @Override
    public StringListenerHandler<ConfigChangeListener> handler() {
        return new StringListenerHandler<ConfigChangeListener>() {
            @Override
            public void addListener(ConfigChangeListener listener) {
                base.handler().addListener(prefix, listener);
            }

            @Override
            public void addListener(Class clazz, ConfigChangeListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void addListener(String prefix, ConfigChangeListener listener) {
                base.handler().addListener(ConfigSection.this.prefix + prefix, listener);
            }

            @Override
            public void removeListener(ConfigChangeListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Collection<ConfigChangeListener> getListeners() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void call(Consumer<ConfigChangeListener> consumer) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void call(String prefix, Consumer<ConfigChangeListener> consumer) {
                base.handler().call(ConfigSection.this.prefix + prefix, consumer);
            }
        };
    }

    @Override
    public String getRawConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRawConfig(String config) {
        throw new UnsupportedOperationException();
    }
}
