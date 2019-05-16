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

    public MasterClientHandler(CloudKit kit) {
        this.kit = kit;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        updateID = kit.getLocalTaskExecutor().runTask(getStatisticsTask(ctx.channel()));
        super.channelRegistered(ctx);
    }

    protected RepeatableTask getStatisticsTask(Channel channel) {
        return new UpdateLocalStatisticsTask(kit, channel);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        kit.getLocalTaskExecutor().stopTask(updateID);
        super.channelUnregistered(ctx);
    }
}
