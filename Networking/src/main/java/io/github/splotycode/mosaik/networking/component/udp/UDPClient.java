package io.github.splotycode.mosaik.networking.component.udp;

import io.github.splotycode.mosaik.networking.component.AbstractClient;
import io.github.splotycode.mosaik.networking.component.IClient;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings({"unused"})
public class UDPClient extends AbstractClient<UDPClient> implements IClient {

    {
        udp = true;
    }

    public static UDPClient create() {
        return new UDPClient();
    }

    @Override
    protected void prepareValues() {
        super.prepareValues();
        if (channelClass == null) {
            channelClass = channelSystem.getDatagramChannelClass();
        }
    }

    @Override
    protected ChannelFuture doBind() {
        return bootstrap.bind(0);
    }

    @Override
    protected void doDefaults() {
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .option(ChannelOption.AUTO_READ, false)
                .option(ChannelOption.SO_BROADCAST, true);
    }

}
