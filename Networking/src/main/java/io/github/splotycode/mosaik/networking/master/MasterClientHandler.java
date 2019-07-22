package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.packet.handle.SelfAnnotationHandler;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.statistics.UpdateLocalStatisticsTask;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class MasterClientHandler extends SelfAnnotationHandler<SerializedPacket> {

    protected final CloudKit kit;
    protected long updateID;
    protected Channel channel;

    public MasterClientHandler(CloudKit kit) {
        this.kit = kit;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        updateID = kit.getLocalTaskExecutor().runTask(new UpdateLocalStatisticsTask(kit, channel));
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        kit.getLocalTaskExecutor().stopTask(updateID);
        super.channelUnregistered(ctx);
    }
}
