package io.github.splotycode.mosaik.networking.util;

import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.config.ConfigurationException;
import io.github.splotycode.mosaik.networking.exception.OutOfPortsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class PortSupplier implements Supplier<Integer> {

    private int minPort, maxPort;

    private LinkedList<Integer> preferredPorts = new LinkedList<>();
    private List<Integer> portBlockList = new ArrayList<>();

    private int currentPort = minPort;

    public static PortSupplier fromConfig(ConfigProvider provider, String prefix) {
        PortSupplier supplier = new PortSupplier(
                provider.getValue(prefix + ".min_port", int.class, 0),
                provider.getValue(prefix + ".max_port", int.class, 0)
        );
        if (supplier.maxPort == 0 || supplier.minPort == 0) {
            throw new ConfigurationException("Invalid port configuration");
        }
        Integer[] preferredPorts = provider.getValue(prefix + ".preferred", Integer[].class, null);
        if (preferredPorts != null) {
            supplier.addPreferedPorts(preferredPorts);
        }
        Integer[] blockedPorts = provider.getValue(prefix + ".blocked", Integer[].class, null);
        if (preferredPorts != null) {
            supplier.addBlockedPorts(blockedPorts);
        }
        return supplier;
    }

    public PortSupplier(int minPort, int maxPort) {
        if (minPort > maxPort) throw new IllegalArgumentException("minPort > maxPort");
        if (minPort < 0) throw new IllegalArgumentException("minPort might not be negative");
        this.minPort = minPort;
        this.maxPort = maxPort;
    }

    public PortSupplier addPreferedPorts(Integer... ports) {
        preferredPorts.addAll(Arrays.asList(ports));
        return this;
    }

    public PortSupplier addBlockedPorts(Integer... ports) {
        portBlockList.addAll(Arrays.asList(ports));
        return this;
    }

    @Override
    public Integer get() {
        Integer prefPort = preferredPorts.poll();
        if (prefPort != null) return prefPort;
        while (true) {
            if (currentPort >= maxPort) throw new OutOfPortsException("Max port (" + maxPort + ") Reached, preferred port list is also empty");
            currentPort++;
            if (!portBlockList.contains(currentPort)) return currentPort;
        }
    }
}
