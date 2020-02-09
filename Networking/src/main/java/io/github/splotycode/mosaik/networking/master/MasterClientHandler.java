package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.packets.UpdateSlavesPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.PacketSerializer;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.network.UpdateLocalStatisticsTask;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class MasterClientHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final CloudKit kit;
    protected long updateID;
    protected Channel channel;

    public MasterClientHandler(CloudKit kit) {
        this.kit = kit;
    }

    @PacketTarget
    public void onSlaveUpdate(UpdateSlavesPacket packet) throws Exception {
        try {
            packet.checkResolvable();
            PacketSerializer serializer = packet.getUnresolvedBody();

            int length = serializer.readVarInt();
            for (int i = 0; i < length; i++) {
                MosaikAddress address = new MosaikAddress(serializer.readString());
                Host rHost = kit.getHostByAddress(address);
                if (rHost instanceof StatisticalHost) {
                    StatisticalHost host = (StatisticalHost) rHost;
                    host.statistics().read(serializer);
                } else if (rHost == null) {
                    throw new IllegalStateException("Could not find host " + address + " present hosts: " + kit.hostMap());
                } else {
                    throw new IllegalStateException("Tried to update non statistical host " + rHost);
                }
            }
        } finally {
            packet.resolveDone();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
        updateID = kit.getLocalTaskExecutor().runTask(new UpdateLocalStatisticsTask(kit, channel));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        kit.getLocalTaskExecutor().stopTask(updateID);
    }
}
