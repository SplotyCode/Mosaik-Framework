package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.host.RemoteMasterHost;
import io.github.splotycode.mosaik.networking.master.packets.UpdateSlavesPacket;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.Channel;

import java.util.HashMap;

public class UpdateSlavesTask extends RepeatableTask {

    private MasterService service;

    public UpdateSlavesTask(MasterService service) {
        super(service.cloudKit().getConfig(MasterService.MASTER_UPDATE_SLAVE_DELAY));
        this.service = service;
    }

    @Override
    public void run() {
        HashMap<MosaikAddress, HostStatistics> statistics = new HashMap<>();
        for (Host host : service.cloudKit().getHosts()) {
            if (host instanceof StatisticalHost) {
                HostStatistics stats = ((StatisticalHost) host).getStatistics();
                if (stats != null) {
                    statistics.put(host.address(), stats);
                }
            }
        }
        for (Host host : service.cloudKit().getHosts()) {
            if (host instanceof RemoteMasterHost) {
                Channel channel = ((RemoteMasterHost) host).getChannel();
                if (channel != null) {
                    HostStatistics stats = statistics.remove(host.address());
                    channel.writeAndFlush(new UpdateSlavesPacket(statistics));
                    if (stats != null) {
                        statistics.put(host.address(), stats);
                    }
                }
            }
        }
    }
}
