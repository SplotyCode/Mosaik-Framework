package io.github.splotycode.mosaik.networking.component;

import io.github.splotycode.mosaik.networking.exception.SecureException;
import io.github.splotycode.mosaik.networking.packet.system.PacketSystem;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLException;
import java.io.File;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractServer<S extends AbstractServer<S>> extends NetworkComponent<ServerBootstrap, ServerChannel, S> {

    protected Map<ChannelOption, Object> childOptions;
    protected final HandlerHolder childHandlers = new HandlerHolder();

    public S sslSelfSigned() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            ssl(ssc.certificate(), ssc.privateKey());
        } catch (CertificateException ex) {
            throw new SecureException("Failed to set SSL Context", ex);
        }
        return self();
    }


    @Override
    protected void applyChannel() {
        bootstrap.channel(channelClass);
    }

    @Override
    protected void prepareValues() {
        super.prepareValues();
        if (sslMode != SSLMode.NONE && sslContext == null) {
            sslSelfSigned();
        }
        if (bootstrap == null) {
            bootstrap = new ServerBootstrap();
        }
    }

    public S ssl(File certificate, File privateKey) {
        try {
            sslContext = SslContextBuilder.forServer(certificate, privateKey).build();
        } catch (SSLException ex) {
            throw new SecureException("Failed to set SSL Context", ex);
        }
        return self();
    }

    @Override
    protected void handlerLogic() {
        super.handlerLogic();
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline pipeline = channel.pipeline();

                ChannelInboundHandler ssl = getSSLHandler(channel);
                if (ssl != null) {
                    pipeline.addLast("ssl", ssl);
                }

                for (HandlerHolder.AbstractHandlerData handler : childHandlers.getHandlerData()) {
                    pipeline.addLast(handler.getName(), handler.handler());
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                ctx.fireExceptionCaught(cause);
            }
        });
    }

    @Override
    protected void optionLogic() {
        super.optionLogic();
        if (childOptions == null) return;
        for (Map.Entry<ChannelOption, Object> entry : childOptions.entrySet()) {
            bootstrap.childOption(entry.getKey(), entry.getValue());
        }
    }

    public S childHandler(int priority, String name, ChannelHandler handler) {
        synchronized (childHandlers) {
            childHandlers.addHandler(priority, name, handler);
        }
        return self();
    }

    public S childHandler(int priority, String name, Class<? extends ChannelHandler> clazz, Supplier<ChannelHandler> obj) {
        synchronized (childHandlers) {
            childHandlers.addHandler(priority, name, clazz, obj);
        }
        return self();
    }

    public <H extends ChannelHandler> H getChildHandler(Class<H> clazz) {
        synchronized (childHandlers) {
            return childHandlers.getHandler(clazz);
        }
    }

    public String getChildHandlerName(Class<? extends ChannelHandler> clazz) {
        synchronized (childHandlers) {
            return childHandlers.getHandlerName(clazz);
        }
    }

    @Override
    public S usePacketSystem(int priority, PacketSystem system) {
        childHandler(priority, "packetSystem", createPacketHandler(system));
        return self();
    }

    public S childOption(Map<ChannelOption, Object> options) {
        childOptions = options;
        return self();
    }

}
