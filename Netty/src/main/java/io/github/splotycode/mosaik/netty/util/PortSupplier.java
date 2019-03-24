package io.github.splotycode.mosaik.netty.util;

import io.github.splotycode.mosaik.netty.exception.OutOfPortsException;
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

    public PortSupplier(int minPort, int maxPort) {
        if (minPort > maxPort) throw new IllegalArgumentException("minPort > maxPort");
        if (minPort < 0) throw new IllegalArgumentException("minPort migth not be negative");
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
            if (currentPort >= maxPort) throw new OutOfPortsException("Max port (" + maxPort + ") Reached");
            currentPort++;
            if (!portBlockList.contains(currentPort)) return currentPort;
        }
    }
}
