package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitComponent;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticService;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;

public interface ManagedComponentService<C extends NetworkComponent> extends ManagedInstancedService<C>, StatisticService, CloudKitComponent {

    @Override
    default ServiceStatistics statistics() {
        ServiceStatistics statistics = new ServiceStatistics((StatisticalHost) cloudKit().getSelfHost(), displayName());
        getInstances().forEach(statistics::addComponent);
        return statistics;
    }

}
