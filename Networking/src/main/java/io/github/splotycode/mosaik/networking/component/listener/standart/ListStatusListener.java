package io.github.splotycode.mosaik.networking.component.listener.standart;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.BindListener;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.StatusListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class ListStatusListener implements StatusListener {

    private Collection<Listener> listeners;

    @Override
    public void bind(NetworkComponent component, AbstractBootstrap bootstrap) {
        listeners.forEach(listener -> {
            if (listener instanceof BindListener) {
                ((BindListener) listener).bind(component, bootstrap);
            }
        });
    }

    @Override
    public void bound(NetworkComponent component, ChannelFuture future) {
        listeners.forEach(listener -> {
            if (listener instanceof BoundListener) {
                ((BoundListener) listener).bound(component, future);
            }
        });
    }

    @Override
    public void unBound(NetworkComponent component, ChannelFuture future) {
        listeners.forEach(listener -> {
            if (listener instanceof UnBoundListener) {
                ((UnBoundListener) listener).unBound(component, future);
            }
        });
    }

}
