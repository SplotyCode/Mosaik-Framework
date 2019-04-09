package io.github.splotycode.mosaik.networking.packet.handle;

import io.github.splotycode.mosaik.networking.packet.Packet;
import io.github.splotycode.mosaik.util.listener.StringListenerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ListenerContentHandler<P extends Packet> extends SimpleChannelInboundHandler<P> {

    private StringListenerHandler<PacketListener<P>> handler = new StringListenerHandler<>();

    public ListenerContentHandler<P> register(String prefix, PacketListener<P> listener) {
        handler.addListener(prefix, listener);
        return this;
    }

    public ListenerContentHandler<P> register(PacketListener<P> listener) {
        handler.addListener(listener);
        return this;
    }

    public ListenerContentHandler<P> register(Class clazz, PacketListener<P> listener) {
        handler.addListener(clazz, listener);
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, P packet) throws Exception {
        handler.call(packet.getClass().getName(), listener -> listener.onPacket(packet));
    }

}
