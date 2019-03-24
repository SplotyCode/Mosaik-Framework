package io.github.splotycode.mosaik.netty.util;

import io.github.splotycode.mosaik.netty.component.NetworkComponent;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class ComponentSupplier<C extends NetworkComponent> implements Supplier<C> {

    protected final ArrayList<C> components = new ArrayList<>();
    protected int maxInstances, startInstances;
    protected Supplier<C> serverStarter;
    protected Supplier<Integer> portSupplier;

    private Random rand = new Random();

    protected void starNewtInstances() {
        C server = serverStarter.get();
        server.port(portSupplier);
        server.onUnBound((component, future) -> components.remove(component.port()));
        server.bind(false);
    }

    @Override
    public synchronized C get() {
        while (startInstances < components.size()) {
            starNewtInstances();
        }
        return components.get(rand.nextInt(components.size()));
    }

    public void shutdown() {
        components.forEach(NetworkComponent::shutdown);
    }
}
