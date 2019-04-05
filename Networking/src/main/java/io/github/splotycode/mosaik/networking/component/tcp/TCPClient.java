package io.github.splotycode.mosaik.networking.component.tcp;

import io.github.splotycode.mosaik.networking.component.AbstractClient;
import io.github.splotycode.mosaik.networking.component.IClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings({"unused"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TCPClient extends AbstractClient<TCPClient> implements IClient {

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
