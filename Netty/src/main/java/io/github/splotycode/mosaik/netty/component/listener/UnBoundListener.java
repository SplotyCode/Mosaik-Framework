package io.github.splotycode.mosaik.netty.component.listener;

import io.github.splotycode.mosaik.netty.component.NetworkComponent;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.channel.ChannelFuture;

public interface UnBoundListener extends Listener {

    void unBound(NetworkComponent component, ChannelFuture future);

}
