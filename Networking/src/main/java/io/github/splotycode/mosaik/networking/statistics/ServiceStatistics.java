package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.network.CodecNetworkProcess;

import java.util.Collection;

public interface ServiceStatistics extends IStatistics, SerializedPacket {

    int FLAG_COSTOM_CODEC = 0x00000001;
    int FLAG_RUNNING = 0x00000002;
    int FLAG_NO_CONNECTION_COUNTER = 0x00000004;

    String serviceName();

    int totalInstances();
    int totalConnections();

    INetworkProcess lowestConnectionInstance();

    Collection<INetworkProcess> getInstances();

    @Override
    default void write(PacketSerializer packet) throws Exception {
        packet.writeVarInt(totalInstances());
        for (INetworkProcess process : getInstances()) {
            packet.writeVarInt(process.port());

            boolean costomCodec = process instanceof CodecNetworkProcess;
            int connections = process.connectionCount();

            /* Write controlByte */
            int controlByte = costomCodec ? FLAG_COSTOM_CODEC : 0;
            if (costomCodec) {
                controlByte |= ((CodecNetworkProcess) process).calculateControlByte();
            }
            if (connections == -1) {
                controlByte |= FLAG_NO_CONNECTION_COUNTER;
            }
            if (process.running()) {
                controlByte |= FLAG_RUNNING;
            }
            packet.writeVarInt(controlByte);

            /* Write Packet Body*/
            if (connections != -1) {
                packet.writeVarInt(connections);
            }
            if (costomCodec) {
                ((CodecNetworkProcess) process).write(packet);
            }

        }
    }

}
