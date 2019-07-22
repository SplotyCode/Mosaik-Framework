package io.github.splotycode.mosaik.networking.packet.system;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ChannelHandler.Sharable
public class PacketSystemHandler extends ChannelInitializer<Channel> {

    private PacketSystem packetSystem;
    private boolean server;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        if (server) {
            packetSystem.initalizeServer(channel.pipeline());
        } else {
            packetSystem.initalizeClient(channel.pipeline());
        }
    }
}
