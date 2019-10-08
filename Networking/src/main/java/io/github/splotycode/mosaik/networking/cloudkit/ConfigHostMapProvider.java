package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigChangeListener;
import io.github.splotycode.mosaik.networking.config.ConfigEntry;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;

import java.util.ArrayList;
import java.util.List;

public class ConfigHostMapProvider extends HostMapProvider implements ConfigChangeListener, ConfigProviderChangeListener {

    public static final ConfigKey<Long> MAX_CACHE = new ConfigKey<>("hosts.max_cache", long.class, 30 * 1000L);

    private String configPath;

    public ConfigHostMapProvider(CloudKit kit, String configPath) {
        super(kit, kit.getConfig(MAX_CACHE));
        this.configPath = configPath;
        newConfigProvider(null, kit.getConfigProvider());
        kit.getHandler().addListener(this);
    }

    @Override
    protected List<String> fill() {
        List<String> hosts = new ArrayList<>();
        for (Object host : kit.getConfigProvider().getValue(configPath, Iterable.class, null)) {
            String address = host.toString();
            if (!kit.localAddress().equals(address)) {
                hosts.add(address);
            }
        }
        return hosts;
    }

    @Override
    public void onChange(String originalUpdate, ConfigEntry entry) {
        clear();
    }

    @Override
    public void newConfigProvider(ConfigProvider old, ConfigProvider newProvider) {
        if (old != null) {
            old.handler().removeListener(this);
        }
        newProvider.handler().addListener(this);
    }
}
