package io.github.splotycode.mosaik.networking.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable
public class CurrentConnectionHandler extends ChannelInboundHandlerAdapter {

    private AtomicLong connections = new AtomicLong();
    private AtomicLong activeConnections = new AtomicLong();

    public long getConnections() {
        return connections.get();
    }

    public long getActiveConnections() {
        return activeConnections.get();
    }

    public AtomicLong getConnectionsAtomic() {
        return connections;
    }

    public AtomicLong getActiveConnectionsAtomic() {
        return activeConnections;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        connections.incrementAndGet();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        connections.decrementAndGet();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        activeConnections.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        activeConnections.decrementAndGet();
    }
}
