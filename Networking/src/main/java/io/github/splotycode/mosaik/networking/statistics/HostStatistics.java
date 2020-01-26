package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public interface HostStatistics extends IStatistics, SerializedPacket {

    Host getHost();

    double getCPULoad();
    long getFreeMemory();

    int totalConnections();
    int totalConnections(Service service);
    int totalConnections(String service);

    int totalInstances();
    int totalInstances(Service service);
    int totalInstances(String service);

    boolean hasService(Service service);
    boolean hasService(String service);

    int totalServices();

    Map<String, ServiceStatistics> serviceMap();
    Collection<String> getServices();
    void forEachService(Consumer<ServiceStatistics> consumer);
    ServiceStatistics getService(Service service);
    ServiceStatistics getService(String service);

    Iterable<INetworkProcess> getInstances();
    Collection<INetworkProcess> getInstances(Service service);
    Collection<INetworkProcess> getInstances(String service);

    @Override
    default void write(PacketSerializer packet) throws Exception {
        packet.writeLong(getFreeMemory());
        packet.writeDouble(getCPULoad());

        packet.writeVarInt(totalServices());
        for (Map.Entry<String, ServiceStatistics> service : serviceMap().entrySet()) {
            packet.writeString(service.getKey());
            service.getValue().write(packet);
        }
    }
}
