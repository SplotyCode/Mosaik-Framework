package me.david.davidlib.util.core.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import me.david.davidlib.util.core.netty.packets.SerializePacket;
import me.david.davidlib.util.core.netty.PacketRegistry;
import me.david.davidlib.util.core.netty.decoder.SerializePacketDecoder;
import me.david.davidlib.util.core.netty.encoder.SerializePacketEncoder;

import java.util.function.Consumer;

public class GeneralChannelInitializer extends ChannelInitializer<SocketChannel> {

    public GeneralChannelInitializer(PacketRegistry<SerializePacket> registry, Consumer<SocketChannel> nextBootstrap) {
        this.nextBootstrap = nextBootstrap;
        this.registry = registry;
    }

    //Channel Variables
    private final Consumer<SocketChannel> nextBootstrap;
    private final PacketRegistry<SerializePacket> registry;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //Grab Pipeline
        ChannelPipeline p = ch.pipeline();

        //Add Decoders
        p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        //p.addLast(new StringDecoder(CharsetUtil.UTF_8));
        p.addLast(new SerializePacketDecoder(registry));

        //Add Encoders
        p.addLast(new LengthFieldPrepender(4));
        //p.addLast(new StringEncoder(CharsetUtil.UTF_8));
        p.addLast(new SerializePacketEncoder(registry));


        nextBootstrap.accept(ch);
    }
}
