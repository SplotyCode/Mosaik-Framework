package io.github.splotycode.mosaik.networking.statistics.component;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitComponent;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.local.AbstractLocalServiceStatistics;

import java.util.Collection;
import java.util.Collections;

public interface SingleComponentService extends StatisticalService, CloudKitComponent {

    INetworkProcess component();

    @Override
    default ServiceStatistics createStatistics() {
        INetworkProcess component = component();
        boolean exists = component != null;
        return new AbstractLocalServiceStatistics(this) {

            private Collection<INetworkProcess> instances;

            {
                if (!exists) {
                    instances = Collections.emptyList();
                }
            }

            @Override
            public String serviceName() {
                return displayName();
            }

            @Override
            public int totalInstances() {
                return exists ? 1 : 0;
            }

            @Override
            public int totalConnections() {
                if (exists) {
                    int connectionCount = component.connectionCount();
                    return connectionCount == -1 ? 0 : connectionCount;
                }
                return 0;
            }

            @Override
            public INetworkProcess lowestConnectionInstance() {
                return component();
            }

            @Override
            public Collection<INetworkProcess> getInstances() {
                if (instances == null) {
                    synchronized (this) {
                        if (instances == null) {
                            instances = Collections.singleton(component);
                        }
                    }
                }
                return instances;
            }
        };
    }

}
