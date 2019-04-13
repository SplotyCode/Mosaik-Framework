package io.github.splotycode.mosaik.networking.config;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.component.listener.BoundListener;
import io.github.splotycode.mosaik.networking.config.packets.ConfigNoUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigRequestUpdate;
import io.github.splotycode.mosaik.networking.config.packets.ConfigUpdate;
import io.github.splotycode.mosaik.networking.config.packets.KAUpdate;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.util.CodecUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConfigClientHandler implements BoundListener {

    private static final Logger LOGGER = Logger.getInstance(ConfigClientHandler.class);

    private ConfigProvider provider;
    private boolean keepAlive;

    @PacketTarget
    public void noUpdate(ConfigNoUpdate packet, ChannelHandlerContext ctx) {
        LOGGER.info("Config is up to date");
        if (!keepAlive) {
            ctx.close();
        }
    }

    @PacketTarget
    public void onKeepAliveUpdate(KAUpdate packet) {
        provider.set(packet.getKey(), packet.getNewValue());
    }

    @PacketTarget
    public void onUpdate(ConfigUpdate packet, ChannelHandlerContext ctx) {
        provider.setRawConfig(packet.getConfig());
        if (!keepAlive) {
            ctx.close();
        }
    }

    @Override
    public void bound(NetworkComponent component, ChannelFuture future) {
        String configHash = CodecUtil.sha1Hex(provider.getRawConfig());
        future.channel().writeAndFlush(new ConfigRequestUpdate(configHash));
    }
}
