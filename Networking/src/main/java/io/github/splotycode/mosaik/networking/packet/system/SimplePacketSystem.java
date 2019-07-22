package io.github.splotycode.mosaik.networking.packet.system;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public abstract class SimplePacketSystem<P extends Packet> implements PacketSystem<P> {

    abstract ChannelHandler decoder();
    abstract ChannelHandler encoder();

    public SimplePacketSystem(PacketRegistry<P> packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    protected PacketRegistry<P> packetRegistry;

    private void initialize(ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(encoder());
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(decoder());
    }

    @Override
    public void initalizeClient(ChannelPipeline pipeline) {
        initialize(pipeline);
    }

    @Override
    public void initalizeServer(ChannelPipeline pipeline) {
        initialize(pipeline);
    }
}
