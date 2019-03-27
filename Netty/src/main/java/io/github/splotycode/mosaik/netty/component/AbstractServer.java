package io.github.splotycode.mosaik.netty.component;

import io.github.splotycode.mosaik.netty.exception.SecureExcpetion;
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

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractServer<S extends AbstractServer<S>> extends NetworkComponent<ServerBootstrap, ServerChannel, S> {

    protected Map<ChannelOption, Object> childOptions;
    protected ChannelHandler childHandler;

    public S sslSelfSigned() {
        try {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            ssl(ssc.certificate(), ssc.privateKey());
        } catch (CertificateException ex) {
            throw new SecureExcpetion("Failed to set SSL Context", ex);
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

    public S ssl(File certificate, File priavteKey) {
        try {
            sslContext = SslContextBuilder.forServer(certificate, priavteKey).build();
        } catch (SSLException ex) {
            throw new SecureExcpetion("Failed to set SSL Context", ex);
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

                if (childHandler != null) {
                    pipeline.addLast("costom", childHandler);
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

    public S childHandler(ChannelHandler handler) {
        childHandler = handler;
        return self();
    }

    public S childOption(Map<ChannelOption, Object> options) {
        childOptions = options;
        return self();
    }

}
