package io.github.splotycode.mosaik.netty.component.listener.standart;

import io.github.splotycode.mosaik.netty.component.NetworkComponent;
import io.github.splotycode.mosaik.netty.component.listener.UnBoundListener;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;

public class ReconnectListener implements UnBoundListener {

    private long reconnectMultiplier;
    private long reconnectMultiplierStart;
    private long reconnectMultiplierMax;

    private long currentReconnect = reconnectMultiplierStart;

    public ReconnectListener() {
        this(2, 4, 16);
    }

    public ReconnectListener(long reconnectMultiplier, long reconnectMultiplierStart, long reconnectMultiplierMax) {
        this.reconnectMultiplier = reconnectMultiplier;
        this.reconnectMultiplierStart = reconnectMultiplierStart;
        this.reconnectMultiplierMax = reconnectMultiplierMax;
    }

    @Override
    public void unBound(NetworkComponent component, ChannelFuture future) {
        if (!future.isSuccess()) {
            future.channel().close();
            currentReconnect = Math.min(reconnectMultiplierMax, currentReconnect *= reconnectMultiplier);
            future.channel().eventLoop().schedule((Runnable) component::bind, currentReconnect, TimeUnit.SECONDS);
        } else {
            currentReconnect = reconnectMultiplierStart;
        }
    }

}
