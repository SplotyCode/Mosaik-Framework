package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;

public interface SingleComponentService extends StatisticService {

    CloudKit kit();

    NetworkComponent component();

    @Override
    default ServiceStatistics statistics() {
        if (component() != null) {
            return new ServiceStatistics((StatisticalHost) kit().getSelfHost(), displayName()).addComponent(component());
        }
        return null;
    }
}
