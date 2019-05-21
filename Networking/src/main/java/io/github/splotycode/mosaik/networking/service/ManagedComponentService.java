package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticService;

public interface ManagedComponentService extends ManagedInstancedService<NetworkComponent>, StatisticService {

    @Override
    default ServiceStatistics statistics() {
        ServiceStatistics statistics = new ServiceStatistics();
        getInstances().forEach(statistics::addComponent);
        return statistics;
    }

}
