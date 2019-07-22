package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticService;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;

public interface ManagedComponentService<C extends NetworkComponent> extends ManagedInstancedService<C>, StatisticService {

    CloudKit kit();

    @Override
    default ServiceStatistics statistics() {
        ServiceStatistics statistics = new ServiceStatistics((StatisticalHost) kit().getSelfHost(), displayName());
        getInstances().forEach(statistics::addComponent);
        return statistics;
    }

}
