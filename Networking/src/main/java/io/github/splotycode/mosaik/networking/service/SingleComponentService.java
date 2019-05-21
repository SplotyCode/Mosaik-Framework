package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticService;

public interface SingleComponentService extends StatisticService {

    NetworkComponent component();

    @Override
    default ServiceStatistics statistics() {
        if (component() != null) {
            return new ServiceStatistics().addComponent(component());
        }
        return null;
    }
}
