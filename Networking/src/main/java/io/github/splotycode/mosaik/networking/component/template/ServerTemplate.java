package io.github.splotycode.mosaik.networking.component.template;

import io.github.splotycode.mosaik.networking.component.AbstractServer;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.component.udp.UDPServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.io.File;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ServerTemplate<I extends AbstractServer<? extends I>> extends ComponentTemplate<ServerTemplate<I>, I> {

    public static ServerTemplate<TCPServer<? extends TCPServer>> tcp() {
        return new ServerTemplate<TCPServer<? extends TCPServer>>() {
            @Override
            public TCPServer<? extends TCPServer> createComponent() {
                return TCPServer.create();
            }
        };
    }

    public static ComponentTemplate<?, UDPServer<? extends UDPServer>> udp() {
        return new ComponentTemplate<ComponentTemplate, UDPServer<? extends UDPServer>>() {
            @Override
            public UDPServer<? extends UDPServer> createComponent() {
                return UDPServer.create();
            }
        };
    }

    public ServerTemplate<I> childHandler(int priority, String name, ChannelHandler handler) {
        tasks.add(i -> i.childHandler(priority, name, handler));
        return self();
    }

    public ServerTemplate<I> childHandler(int priority, String name, Class<? extends ChannelHandler> clazz, Supplier<ChannelHandler> obj) {
        tasks.add(i -> i.childHandler(priority, name, clazz, obj));
        return self();
    }

    public ServerTemplate<I> childOption(Map<ChannelOption, Object> options) {
        tasks.add(i -> i.childOption(options));
        return self();
    }

    public ServerTemplate<I> sslSelfSigned() {
        tasks.add(AbstractServer::sslSelfSigned);
        return self();
    }

    public ServerTemplate<I> ssl(File certificate, File priavteKey) {
        tasks.add(i -> i.ssl(certificate, priavteKey));
        return self();
    }

}
