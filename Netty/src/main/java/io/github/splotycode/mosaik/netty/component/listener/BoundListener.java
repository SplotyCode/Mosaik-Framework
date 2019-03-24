package io.github.splotycode.mosaik.netty.component.listener;

import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.channel.ChannelFuture;

public interface BoundListener extends Listener {

    void bound(ChannelFuture channel);

}
