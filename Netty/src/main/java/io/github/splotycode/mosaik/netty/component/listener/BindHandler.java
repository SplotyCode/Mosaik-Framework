package io.github.splotycode.mosaik.netty.component.listener;

import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.bootstrap.ServerBootstrap;

public interface BindHandler extends Listener {

    void bind(ServerBootstrap bootstrap);

}
