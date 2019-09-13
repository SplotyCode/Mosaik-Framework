package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitComponent;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;

public interface SingleComponentService extends StatisticService, CloudKitComponent {

    INetworkProcess component();

    @Override
    default ServiceStatistics statistics() {
        if (component() != null) {
            return new ServiceStatistics((StatisticalHost) cloudKit().getSelfHost(), displayName()).addComponent(component());
        }
        return null;
    }
}
