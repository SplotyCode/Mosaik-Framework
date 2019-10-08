package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.packets.UpdateSlavesPacket;
import io.github.splotycode.mosaik.networking.packet.handle.PacketTarget;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.HostStatisticListener;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.UpdateLocalStatisticsTask;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

@ChannelHandler.Sharable
public class MasterClientHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final CloudKit kit;
    protected long updateID;
    protected Channel channel;

    public MasterClientHandler(CloudKit kit) {
        this.kit = kit;
    }

    @PacketTarget
    public void onSlaveUpdate(UpdateSlavesPacket packet) {
        for (Map.Entry<MosaikAddress, HostStatistics> data : packet.getStatistics().entrySet()) {
            Host rHost = kit.hostMap().get(data.getKey());
            if (rHost instanceof StatisticalHost) {
                StatisticalHost host = (StatisticalHost) rHost;
                host.update(data.getValue());
                kit.getHandler().call(HostStatisticListener.class, listener -> listener.update(host));
            } else if (rHost == null) {
                throw new IllegalStateException("Could not find host  " + data.getKey() + " present hosts: " + kit.hostMap());
            } else {
                throw new IllegalStateException("Tried to update non statistical host " + rHost);
            }
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
