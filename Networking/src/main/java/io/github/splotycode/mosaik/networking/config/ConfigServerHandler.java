package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.component.listener.UnBoundListener;
import io.github.splotycode.mosaik.networking.config.packets.ConfigNoUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigRequestUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigUpdate;
import io.github.splotycode.mosaik.networking.config.packets.KAUpdate;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.util.CodecUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;

@Getter
public class ConfigServerHandler implements BoundListener, UnBoundListener, ConfigChangeListener {

    private final ConfigProvider provider;
    private final boolean keepAlive;

    public ConfigServerHandler(ConfigProvider provider, boolean keepAlive) {
        this.provider = provider;
        this.keepAlive = keepAlive;
    }

    private DefaultChannelGroup kaChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @PacketTarget
    public void onRequestUpdate(ConfigRequestUpdate packet, ChannelHandlerContext ctx) {
        String currentHash = CodecUtil.sha1Hex(provider.getRawConfig());
        if (currentHash.equals(packet.getHash())) {
            ctx.writeAndFlush(new ConfigNoUpdate());
        } else {
            ctx.writeAndFlush(new ConfigUpdate(provider.getRawConfig()));
        }
        if (keepAlive) {
            kaChannels.add(ctx.channel());
        }
    }

    @Override
    public void bound(NetworkComponent component, ChannelFuture future) {
        if (keepAlive) {
            provider.handler().addListener("*", this);
        }
    }

    @Override
    public void unBound(NetworkComponent component, ChannelFuture future) {
        if (keepAlive) {
            provider.handler().removeListener(this);
        }
    }

    @Override
    public void onChange(String originalUpdate, ConfigEntry entry) {
        kaChannels.writeAndFlush(new KAUpdate(entry.configKey(), entry.stringValue()));
    }
}
