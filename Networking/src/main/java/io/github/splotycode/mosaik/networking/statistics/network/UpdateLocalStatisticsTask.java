package io.github.splotycode.mosaik.networking.statistics.network;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.packets.UpdateHostPacket;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;

@Getter
public class UpdateLocalStatisticsTask extends RepeatableTask {

    private final CloudKit kit;
    private final Channel channel;

    public UpdateLocalStatisticsTask(CloudKit kit, Channel channel) {
        this(kit.getConfig(MasterService.DAEMON_STATS_DELAY), kit, channel);
    }

    public UpdateLocalStatisticsTask(long delay, CloudKit kit, Channel channel) {
        super(delay);
        this.channel = channel;
        this.kit = kit;
    }

    @Override
    public void run() {
        channel.writeAndFlush(new UpdateHostPacket(((StatisticalHost) kit.getSelfHost()).statistics())).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }


}
