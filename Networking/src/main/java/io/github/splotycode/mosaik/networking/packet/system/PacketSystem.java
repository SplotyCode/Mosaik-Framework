package io.github.splotycode.mosaik.networking.packet.system;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.netty.channel.ChannelPipeline;

public interface PacketSystem<P extends Packet> {

    void initalizeClient(ChannelPipeline pipeline);
    void initalizeServer(ChannelPipeline pipeline);

}
