package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.template.ComponentTemplate;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public abstract class SimpleInstanceService<S extends ComponentTemplate<S, C>, C extends NetworkComponent<? ,?, C>> implements ManagedInstancedService<C> {

    @Getter protected final ArrayList<C> instances = new ArrayList<>();

    public void startNewInstance() {
        instances.add(componentTemplate().createComponent().bind());
    }

    public abstract ComponentTemplate<S,C> componentTemplate();

    public void updateComponents() {
        /* Make sure we have the minimum amount of Instances */
        while (instances.size() < startupInstances()) {
            startNewInstance();
        }

        /* If all instances less connections then the  optimalConnections and maxInstances() is not reach start new instance */
        LongSummaryStatistics stats = instances.stream().map(c -> c.getHandler(CurrentConnectionHandler.class).getConnections()).collect(Collectors.summarizingLong(Long::longValue));
        if (stats.getMin() > optimalConnections() && instances.size() < maxInstances()) {
            startNewInstance();
        }
    }

    @Override
    public void stop() {
        instances.forEach(NetworkComponent::shutdown);
    }

}
