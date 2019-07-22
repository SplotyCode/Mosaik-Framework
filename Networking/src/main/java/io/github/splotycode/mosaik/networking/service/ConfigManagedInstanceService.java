package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.util.PortSupplier;

import java.util.function.Supplier;

public abstract class ConfigManagedInstanceService<C> implements ManagedInstancedService<C> {

    private PortSupplier portSupplier;
    private int optimalInstances, startupInstances, maxInstances;

    protected String prefix;
    protected CloudKit kit;

    public ConfigManagedInstanceService(String prefix, CloudKit kit) {
        this.prefix = prefix;
        this.kit = kit;
        portSupplier = PortSupplier.fromConfig(kit.getConfigProvider(), prefix);
        optimalInstances = kit.getConfigProvider().getConfigValue(prefix + ".optimal_instances", int.class, -1);
        startupInstances = kit.getConfigProvider().getConfigValue(prefix + ".startup_instances", int.class, -1);
        maxInstances = kit.getConfigProvider().getConfigValue(prefix + ".max_instances", int.class, -1);
    }

    @Override
    public int optimalConnections() {
        return optimalInstances;
    }

    @Override
    public int startupInstances() {
        return startupInstances;
    }

    @Override
    public int maxInstances() {
        return maxInstances;
    }

    @Override
    public Supplier<Integer> portSupplier() {
        return portSupplier;
    }

}
