package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitComponent;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalService;
import io.github.splotycode.mosaik.networking.statistics.local.DefaultLocalServiceStatistics;

import java.util.Collection;

public interface ManagedComponentService<C extends NetworkComponent> extends ManagedInstancedService<C>, StatisticalService, CloudKitComponent {

    @Override
    @SuppressWarnings("unchecked")
    default ServiceStatistics createStatistics() {
        return new DefaultLocalServiceStatistics(this, (Collection<INetworkProcess>) (Collection<?>) getInstances());
    }
}
