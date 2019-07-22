package io.github.splotycode.mosaik.networking.loadbalance;

import io.github.splotycode.mosaik.networking.component.template.ComponentTemplate;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.Instance;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.util.collection.RoundRobin;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.netty.channel.Channel;

import java.util.*;

public class CPUBalanceStrategy implements LoadBalanceStrategy {

    private static final Logger LOGGER = Logger.getInstance(CPUBalanceStrategy.class);

    private long minRam;
    private int optimalConnections;
    private int capIncrease;
    private int maxCapIncrease;
    private int minInstances;
    private int roundRobinSize;
    private int maxConnections;
    private ComponentTemplate template;

    public CPUBalanceStrategy(long minRam, int optimalConnections, int capIncrease,
                              int maxCapIncrease, int minInstances, int roundRobinSize,
                              int maxConnections, ComponentTemplate template) {
        this.minRam = minRam;
        this.optimalConnections = optimalConnections;
        this.capIncrease = capIncrease;
        this.maxCapIncrease = maxCapIncrease;
        this.minInstances = minInstances;
        this.roundRobinSize = roundRobinSize;
        this.maxConnections = maxConnections;
        this.template = template;
    }

    private volatile Iterator<Instance> iterable;

    @Override
    public void update(LoadBalancerService service) {
        /* Grab a list of all online hosts that have minRam, use statistics and have redirect service instances*/
        ArrayList<StatisticalHost> hosts = new ArrayList<>();
        for (Host host : service.getKit().getHosts()) {
            if (host instanceof StatisticalHost) {
                HostStatistics statistics = ((StatisticalHost) host).getStatistics();
                if (host.isOnline() && statistics.getFreeRam() >= minRam && statistics.hasService(service.getRedirectService())) {
                    hosts.add((StatisticalHost) host);
                }
            }
        }
        /* Sort by CPU and cut of*/
        hosts.sort(Comparator.comparingDouble(host -> ((StatisticalHost) host).getStatistics().getCpu()).reversed());
        hosts.subList(Math.min(roundRobinSize, hosts.size()), hosts.size()).clear();

        /* Collect instances */
        ArrayList<Instance> instances = new ArrayList<>();

        int cap = optimalConnections;
        int lastCap = 0;
        int capIndex = 0;
        while ((capIndex < maxCapIncrease || instances.size() < minInstances) && cap <= maxConnections) {
            for (StatisticalHost host : hosts) {
                for (Instance instance : host.getStatistics().getConnection(service.getRedirectService())) {
                    if (instance.getConnections() > lastCap && instance.getConnections() <= cap) {
                        instances.add(instance);
                    }
                }
            }
            capIndex++;
            lastCap = cap;
            cap += capIncrease;
        }

        iterable = new RoundRobin<>(instances).iterator();
    }


    private Map<Instance, Channel> channels = new HashMap<>();

    private Channel getChannel(Instance instance) {
        return channels.computeIfAbsent(instance, dummy -> template.createComponent().address(instance.getHost().address().asSocketAddress(instance.getPort())).bind(true).nettyFuture().channel());
    }

    @Override
    public Channel nextServer() {
        while (iterable.hasNext()) {
            Instance instance = iterable.next();
            if (instance.getHost().isOnline()) {
                return getChannel(instance);
            }
        }
        LOGGER.warn("No instance found");
        return null;
    }
}
