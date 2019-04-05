package io.github.splotycode.mosaik.networking.component.listener;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.bootstrap.AbstractBootstrap;

public interface BindListener<B extends AbstractBootstrap> extends Listener {

    void bind(NetworkComponent component, B bootstrap);

}
