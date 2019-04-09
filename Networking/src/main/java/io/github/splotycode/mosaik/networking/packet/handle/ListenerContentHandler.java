package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.listener.StringListenerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ListenerContentHandler extends SimpleChannelInboundHandler<Packet> {

    private StringListenerHandler<PacketListener> handler = new StringListenerHandler<>();

    public ListenerContentHandler register(String prefix, PacketListener listener) {
        handler.addListener(prefix, listener);
        return this;
    }

    public ListenerContentHandler register(PacketListener listener) {
        handler.addListener(listener);
        return this;
    }

    public ListenerContentHandler register(Class clazz, PacketListener listener) {
        handler.addListener(clazz, listener);
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        handler.call(packet.getClass().getName(), listener -> listener.onPacket(packet));
    }

}
