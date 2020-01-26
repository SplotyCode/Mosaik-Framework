package io.github.splotycode.mosaik.networking.statistics.remote;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.LazyLoad;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RemoteServiceStatistics extends AbstractRemoteStatistics implements ServiceStatistics {

    protected Map<Integer, RemoteInstance> instances = new HashMap<>();
    private StatisticalHost host;
    private String service;

    private Cache<Integer> totalConnections = new LazyLoad<Integer>() {
        @Override
        protected Integer initialize() {
            int total = 0;
            for (INetworkProcess process : instances.values()) {
                int connections = process.connectionCount();
                if (connections != -1) {
                    total += connections;
                }
            }
            return total;
        }
    };
    private Cache<RemoteInstance> lowestConnectionInstance = new LazyLoad<RemoteInstance>() {
        @Override
        protected RemoteInstance initialize() {
            RemoteInstance best = null;
            int bestConnections = -1;
            for (RemoteInstance process : instances.values()) {
                int connections = process.connectionCount();
                if (connections != -1 && (best == null || connections < bestConnections)) {
                    best = process;
                    bestConnections = connections;
                }
            }
            return best;
        }
    };

    public RemoteServiceStatistics(StatisticalHost host, String service) {
        this.host = host;
        this.service = service;
    }

    @Override
    public void read(PacketSerializer serializer) {
        int instancesSize = serializer.readVarInt();
        for (int i = 0; i < instancesSize; i++) {
            int port = serializer.readVarInt();
            int controlByte = serializer.readVarInt();

            RemoteInstance instance = instances.get(port);
            if (instance == null) {
                instance = new RemoteInstance(port, host, service);
            }
            instance.update(controlByte, serializer);
        }
    }

    @Override
    public String serviceName() {
        return service;
    }

    @Override
    public int totalInstances() {
        return instances.size();
    }

    @Override
    public int totalConnections() {
        return totalConnections.getValue();
    }

    @Override
    public INetworkProcess lowestConnectionInstance() {
        return lowestConnectionInstance.getValue();
    }

    @Override
    public Collection<INetworkProcess> getInstances() {
        //noinspection unchecked
        return (Collection<INetworkProcess>) (Collection<?>) instances.values();
    }

    @Override
    public void triggerUpdate() {
        super.triggerUpdate();
        totalConnections.clear();
        lowestConnectionInstance.clear();
    }

}
