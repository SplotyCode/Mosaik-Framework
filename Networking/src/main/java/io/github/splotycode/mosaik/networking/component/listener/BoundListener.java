package io.github.splotycode.mosaik.networking.component.listener;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.channel.ChannelFuture;

public interface BoundListener extends Listener {

    void bound(NetworkComponent component, ChannelFuture future);

}
