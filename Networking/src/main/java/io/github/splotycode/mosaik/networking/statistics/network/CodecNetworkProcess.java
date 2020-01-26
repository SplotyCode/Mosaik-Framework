package io.github.splotycode.mosaik.networking.statistics.network;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;

public interface CodecNetworkProcess extends INetworkProcess, SerializedPacket {

    int calculateControlByte();
    void applyControlByte(int controlByte);

    @Override
    default void read(PacketSerializer packet) throws Exception {

    }

    @Override
    default void write(PacketSerializer packet) throws Exception {

    }

}
