package io.github.splotycode.mosaik.networking.component.template;

import io.github.splotycode.mosaik.networking.component.AbstractServer;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.component.udp.UDPServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.io.File;
import java.util.Map;

public abstract class ServerTemplate<S extends ServerTemplate<S, I>, I extends AbstractServer<I>> extends ComponentTemplate<S, I> {

    private static class TCPServerTemplate extends ServerTemplate<TCPServerTemplate, TCPServer> {

        @Override
        public TCPServer createComponent() {
            return TCPServer.create();
        }
    }

    private static class UDPServerTemplate extends ComponentTemplate<UDPServerTemplate, UDPServer> {

        @Override
        public UDPServer createComponent() {
            return UDPServer.create();
        }
    }

    public static ServerTemplate<?, TCPServer> tcp() {
        return new TCPServerTemplate();
    }

    public static ComponentTemplate<?, UDPServer> udp() {
        return new UDPServerTemplate();
    }

    public S childHandler(ChannelHandler handler) {
        tasks.add(i -> i.childHandler(handler));
        return self();
    }

    public S childOption(Map<ChannelOption, Object> options) {
        tasks.add(i -> i.childOption(options));
        return self();
    }

    public S sslSelfSigned() {
        tasks.add(AbstractServer::sslSelfSigned);
        return self();
    }

    public S ssl(File certificate, File priavteKey) {
        tasks.add(i -> i.ssl(certificate, priavteKey));
        return self();
    }

}
