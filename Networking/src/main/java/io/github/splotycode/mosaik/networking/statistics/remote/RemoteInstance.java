package io.github.splotycode.mosaik.networking.statistics.remote;

import io.github.splotycode.mosaik.networking.master.host.MasterHost;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.statistics.component.AbstractStatefulProcess;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import lombok.Getter;

import static io.github.splotycode.mosaik.networking.statistics.ServiceStatistics.*;

public class RemoteInstance extends AbstractStatefulProcess {

    private int connections, port;
    private final StatisticalHost host;
    @Getter private final String service;

    @Getter private long lastUpdate;
    
    private boolean supportStateful;
    private boolean running;

    public RemoteInstance(int port, StatisticalHost host, String service) {
        this.port = port;
        this.host = host;
        this.service = service;
    }

    @Override
    public int connectionCount() {
        return connections;
    }

    protected void update(int controlByte, PacketSerializer packet) {
        lastUpdate = System.currentTimeMillis();
        if ((controlByte & FLAG_NO_CONNECTION_COUNTER) == 0) {
            connections = packet.readVarInt();
        } else {
            connections = -1;
        }
        supportStateful = (controlByte & FLAG_COSTOM_CODEC) != 0;
        if (supportStateful) {
            handleCodec(controlByte, packet);
        } else {
            running = (controlByte & FLAG_RUNNING) != 0;
        }
    }

    protected void handleCodec(int controlByte, PacketSerializer packet) {
        applyControlByte(controlByte);
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public boolean running() {
        return supportStateful ? super.running() : running;
    }

    @Override
    public void stop() {
        shuttingDown = true;
        if (host instanceof MasterHost) {
            ((MasterHost) host).stopService(service, port);
        }
    }

    @Override
    public StatisticalHost host() {
        return host;
    }

    @Override
    public RemoteHostStatistics hostStatistics() {
        return (RemoteHostStatistics) host.statistics();
    }

}
