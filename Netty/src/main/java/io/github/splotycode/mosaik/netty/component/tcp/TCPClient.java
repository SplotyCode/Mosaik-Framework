package io.github.splotycode.mosaik.netty.component.tcp;

import io.github.splotycode.mosaik.netty.component.AbstractClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TCPClient<S extends TCPClient<S>> extends AbstractClient<S> {

    public static TCPClient create() {
        return new TCPClient();
    }

    @Override
    protected void prepareValues() {
        super.prepareValues();
        if (channelClass == null) {
            channelClass = channelSystem.getChannelClass();
        }
    }

    @Override
    protected ChannelFuture doBind() {
        return bootstrap.connect(address);
    }

    @Override
    protected void doDefaults() {
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .option(ChannelOption.AUTO_READ, false);
    }

}
