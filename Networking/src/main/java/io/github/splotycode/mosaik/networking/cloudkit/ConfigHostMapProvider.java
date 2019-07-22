package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.config.ConfigChangeListener;
import io.github.splotycode.mosaik.networking.config.ConfigEntry;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;

public class ConfigHostMapProvider extends HostMapProvider implements ConfigChangeListener, ConfigProviderChangeListener {

    public static final ConfigKey<Long> MAX_CACHE = new ConfigKey<>("hosts.max_cache", long.class, 30 * 1000L);

    private String configpath;

    public ConfigHostMapProvider(CloudKit kit, String configPath) {
        super(kit, kit.getConfig(MAX_CACHE));
        this.configpath = configPath;
        newConfigProvider(null, kit.getConfigProvider());
        kit.getHandler().addListener(this);
    }

    @Override
    protected void fill() {
        for (Object host : kit.getConfigProvider().getConfigValue(configpath, Iterable.class, null)) {
            String address = host.toString();
            if (!kit.localAddress().equals(address)) {
                addHost(kit.getHostProvider().provide(kit, host.toString()));
            }
        }
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
