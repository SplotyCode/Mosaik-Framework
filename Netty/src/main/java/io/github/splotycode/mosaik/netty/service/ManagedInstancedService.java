package io.github.splotycode.mosaik.netty.service;

import java.util.Collection;
import java.util.function.Supplier;

public interface ManagedInstancedService<C> extends Service {

    int optimalConnections();
    int startupInstances();
    int maxInstances();

    Supplier<Integer> portSupplier();

    Collection<C> getInstances();

}
