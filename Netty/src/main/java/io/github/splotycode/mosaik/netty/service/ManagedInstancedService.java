package io.github.splotycode.mosaik.netty.service;

import io.github.splotycode.mosaik.netty.component.NetworkComponent;

import java.util.Collection;
import java.util.function.Supplier;

public interface ManagedInstancedService<C extends NetworkComponent<? ,?, C>> extends Service {

    int optimalConnections();
    int startupInstances();
    int maxInstances();

    Supplier<Integer> portSupplier();

    void stopAll();

    Collection<C> getInstances();

}
