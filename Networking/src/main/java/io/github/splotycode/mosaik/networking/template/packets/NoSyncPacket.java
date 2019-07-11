package io.github.splotycode.mosaik.networking.template.packets;

import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;

public class NoSyncPacket implements SerializedPacket {

    @Override public void read(PacketSerializer packet) throws Exception {}

    @Override public void write(PacketSerializer packet) throws Exception {}

}
