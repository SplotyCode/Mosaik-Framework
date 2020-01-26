package io.github.splotycode.mosaik.networking.statistics.local;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractLocalServiceStatistics extends AbstractLocalStatistics implements ServiceStatistics {

    @Getter
    protected StatisticalService service;

    @Override
    public void read(PacketSerializer packet) throws Exception {
        throw new IllegalStateException("Tried to update LocalService over network");
    }

    @Override
    public String serviceName() {
        return service.displayName();
    }

}
