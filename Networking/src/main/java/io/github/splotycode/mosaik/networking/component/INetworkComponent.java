package io.github.splotycode.mosaik.networking.component;

import io.github.splotycode.mosaik.networking.component.listener.BindListener;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.networking.packet.system.PacketSystem;
import io.github.splotycode.mosaik.util.listener.Listener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;

import java.util.Map;
import java.util.function.Supplier;

public interface INetworkComponent<S extends INetworkComponent> {

    S self();

    S port(Supplier<Integer> port);

    <O> S option(ChannelOption<O> option, O value);
    S option(Map<ChannelOption, Object> options);

    S onBind(BindListener listener);
    S onBound(BoundListener listener);
    S onUnBound(UnBoundListener listener);

    S addListener(Listener listener);
    S removeListener(Listener listener);

    S handler(int priority, String name, ChannelHandler handler);
    S removeHandler(int priority);
    S removeHandler(Class<? extends ChannelHandler> clazz);

    S channelSystem(ChannelSystem channelSystem);

    S applyDefaults(boolean apply);

    S noSSL();
    S ssl(SSLMode sslMode);

    default S logging() {
        return logging(true);
    }

    S logging(boolean logging);
    S logging(String category);
    S logging(String category, LogLevel logLevel);
    S noLogging();

    S nThreads(int nThreads);

    S usePacketSystem(int priority, PacketSystem system);

    String displayName();
    S setDisplayName(String displayName);

}
