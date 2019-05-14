package io.github.splotycode.mosaik.networking.statistics;

import com.sun.management.OperatingSystemMXBean;
import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.master.packets.UpdateStatusPacket;
import io.github.splotycode.mosaik.networking.service.ManagedComponentService;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.service.SingleComponentService;
import io.github.splotycode.mosaik.networking.util.CurrentConnectionHandler;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Getter
public class UpdateLocalStatisticsTask extends RepeatableTask {

    private final CloudKit kit;
    private final ChannelHandlerContext ctx;

    @Override
    public void run() {
        int cpu = getCpuLoad();
        long freeRam = Runtime.getRuntime().freeMemory();
        Map<String, Map<Integer, Integer>> connections = new HashMap<>();
        for (Service service : kit.getServices()) {
            Map<Integer, Integer> serviceConnections = getConnections(service);
            if (serviceConnections != null) {
                connections.put(service.displayName(), serviceConnections);
            }
        }
        ctx.writeAndFlush(new UpdateStatusPacket(freeRam, cpu, connections));
    }

    private Map<Integer, Integer> getConnections(Service service) {
        if (service instanceof CostomStatisticService) {
            return ((CostomStatisticService) service).statistics();
        }
        if (service instanceof SingleComponentService) {
            NetworkComponent<?, ? ,?> component = ((SingleComponentService) service).component();
            if (component != null && component.running()) {
                CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
                return CollectionUtil.newHashMap(new Pair<>(component.port(), (int) handler.getConnections()));
            }
        }
        if (service instanceof ManagedComponentService) {
            Map<Integer, Integer> connections = new HashMap<>();
            for (NetworkComponent<?, ?, ?> component : ((ManagedComponentService) service).getInstances()) {
                if (!component.running()) continue;
                CurrentConnectionHandler handler = component.getHandler(CurrentConnectionHandler.class);
                connections.put(component.port(), (int) handler.getConnections());
            }
            return connections;
        }
        return null;
    }

    public int getCpuLoad() {
       return  (int) Math.round(((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad());
    }

    public UpdateLocalStatisticsTask(long delay, CloudKit kit, ChannelHandlerContext ctx) {
        super(delay);
        this.ctx = ctx;
        this.kit = kit;
    }
}
