package io.github.splotycode.mosaik.netty.util;

import io.github.splotycode.mosaik.netty.component.NetworkComponent;
import io.github.splotycode.mosaik.netty.component.template.ComponentTemplate;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class ComponentSupplier<C extends NetworkComponent<? ,?, C>> implements Supplier<C> {

    protected final ArrayList<C> components = new ArrayList<>();
    protected int maxInstances, startInstances;
    protected ComponentTemplate<?, C> template;
    protected Supplier<Integer> portSupplier;

    private Random rand = new Random();

    public ComponentSupplier(int maxInstances, int startInstances, ComponentTemplate<?, C> template, Supplier<Integer> portSupplier) {
        this.maxInstances = maxInstances;
        this.startInstances = startInstances;
        this.template = template;
        this.portSupplier = portSupplier;
    }

    {
        template.onUnBound((component, future) -> components.remove(component.port()));
    }

    protected void starNewtInstances() {
        components.add(template.createComponent().port(portSupplier).bind());
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
