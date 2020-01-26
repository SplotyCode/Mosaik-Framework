package io.github.splotycode.mosaik.networking.statistics.local;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalService;
import lombok.Getter;

import java.util.Collection;

public class DefaultLocalServiceStatistics extends AbstractLocalServiceStatistics {

    @Getter private Collection<INetworkProcess> instances;

    public DefaultLocalServiceStatistics(StatisticalService service, Collection<INetworkProcess> instances) {
        super(service);
        this.instances = instances;
    }

    @Override
    public int totalInstances() {
        return instances.size();
    }

    @Override
    public int totalConnections() {
        int total = 0;
        for (INetworkProcess process : instances) {
            int connections = process.connectionCount();
            if (connections != -1) {
                total += connections;
            }
        }
        return total;
    }

    @Override
    public INetworkProcess lowestConnectionInstance() {
        INetworkProcess best = null;
        int bestConnections = -1;
        for (INetworkProcess process : instances) {
            int connections = process.connectionCount();
            if (connections != -1 && (best == null || connections < bestConnections)) {
                best = process;
                bestConnections = connections;
            }
        }
        return best;
    }



}
