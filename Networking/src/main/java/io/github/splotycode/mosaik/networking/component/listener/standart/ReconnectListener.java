package io.github.splotycode.mosaik.networking.component.listener.standart;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ReconnectListener implements UnBoundListener, BoundListener {

    private static Logger logger = Logger.getInstance(ReconnectListener.class);

    private float reconnectMultiplier;

    private long reconnectMultiplierStart;
    private long reconnectMultiplierMax;

    private volatile AtomicLong currentReconnectBound = new AtomicLong();
    private volatile AtomicLong currentReconnectUnBound = new AtomicLong();

    public ReconnectListener() {
        this(2, 4, 16);
    }

    public ReconnectListener(float reconnectMultiplier, long reconnectMultiplierStart, long reconnectMultiplierMax) {
        this.reconnectMultiplier = reconnectMultiplier;
        this.reconnectMultiplierStart = reconnectMultiplierStart;
        this.reconnectMultiplierMax = reconnectMultiplierMax;
        currentReconnectBound.set(reconnectMultiplierStart);
        currentReconnectUnBound.set(reconnectMultiplierStart);
    }

    @Override
    public void unBound(NetworkComponent component, ChannelFuture future) {
        handleReconnect(component, future, false);
    }

    @Override
    public void bound(NetworkComponent component, ChannelFuture future) {
        handleReconnect(component, future, true);
    }

    private void handleReconnect(NetworkComponent component, ChannelFuture future, boolean bound) {
        AtomicLong atomicDelay = bound ? currentReconnectBound : currentReconnectUnBound;
        if (!future.isSuccess()) {
            atomicDelay.getAndUpdate(reconnectDelay -> {
                logger.info("Reconnecting " + component.getClass().getSimpleName() + " (Triggered By: " + (bound ? "Bound" : "Unbound") + ") in " + reconnectDelay + " seconds...");
                future.channel().eventLoop().schedule(() -> {
                    logger.info("Reconnection " + component.getClass().getSimpleName() + " now");
                    component.bind(true);
                    future.channel().eventLoop().shutdownGracefully();
                }, reconnectDelay, TimeUnit.SECONDS);
                return Math.round(Math.min(reconnectMultiplierMax, reconnectDelay * reconnectMultiplier));
            });
        } else {
            atomicDelay.set(reconnectMultiplierStart);
        }
    }


}
