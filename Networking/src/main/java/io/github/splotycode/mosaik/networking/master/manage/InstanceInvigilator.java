package io.github.splotycode.mosaik.networking.master.manage;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.config.ConfigProvider;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.master.host.MasterHost;
import io.github.splotycode.mosaik.networking.statistics.CloudStatistics;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatefulNetworkProcess;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

@AllArgsConstructor
public class InstanceInvigilator {

    private static final Logger LOGGER = Logger.getInstance(InstanceInvigilator.class);

    public static InstanceInvigilator fromConfig(MasterInstanceService service, String prefix) {
        ConfigProvider provider = service.kit.getConfigProvider();
        return new InstanceInvigilator(service,
                provider.getValue(prefix + ".startupInstances", int.class, 0),
                provider.getValue(prefix + ".optimalConnections", int.class),
                provider.getValue(prefix + ".maxInstances", int.class),
                provider.getValue(prefix + ".stopThreshold", int.class),
                provider.getValue(prefix + ".maxStop", double.class),
                provider.getValue(prefix + ".minimumRam", long.class),
                provider.getValue(prefix + ".onePerMaster", boolean.class, false));
    }

    private MasterInstanceService service;

    private int startupInstances, optimalConnections, maxInstances;

    private int stopThreshold;
    /* In percentage */
    private double maxStop;

    private long minimumRam;

    private boolean onePerMaster;

    protected void startNewInstance() {
        Optional<Host> oHost = service.getKit().getHosts().stream().filter(host -> {
            if (host instanceof MasterHost) {
                HostStatistics statistics = ((MasterHost) host).statistics();
                if (!onePerMaster || statistics.totalInstances(service) == 0) {
                    return ((StatisticalHost) host).statistics().getFreeMemory() >= minimumRam;
                }
            }
            return false;
        }).max(Comparator.comparingDouble(o -> ((StatisticalHost) o).statistics().getCPULoad()));
        if (oHost.isPresent()) {
            MasterHost host = (MasterHost) oHost.get();
            LOGGER.info("New instance for " + service.displayName() + " on " + host.toString());
            host.startNewInstance(service);
        } else {
            LOGGER.info("Not enough resources for " + service.displayName());
        }
    }

    public void updateComponents() {
        CloudStatistics statistics = service.getKit().getStatistics();
        int instances = statistics.getTotalInstances(service);
        ArrayList<INetworkProcess> under = statistics.getInstancesUnder(service, optimalConnections);

        if (under.size() >= stopThreshold) {
            int close = (int) Math.min(under.size(), instances * maxStop);
            int closed = 0;
            Iterator<INetworkProcess> iterator = under.iterator();
            while (iterator.hasNext() && closed <= close) {
                INetworkProcess instance = iterator.next();
                if (instance.hostStatistics().getFreeMemory() <= minimumRam && !((StatefulNetworkProcess) instance).shuttingDown()) {
                    closed++;
                    iterator.remove();
                    instance.stop();
                }
            }
            int i = 0;
            while (closed <= close && i < 10_000) {
                ArrayList<StatisticalHost> hosts = new ArrayList<>();
                for (INetworkProcess instance : under) {
                    StatisticalHost host = (StatisticalHost) instance.host();
                    if (!hosts.contains(host) && !((StatefulNetworkProcess) instance).shuttingDown()) {
                        hosts.add(host);
                    }
                }
                hosts.sort(Comparator.comparingDouble(o -> o.statistics().getCPULoad()));
                for (StatisticalHost host : hosts) {
                    INetworkProcess instance = host.statistics().getService(service).lowestConnectionInstance();
                    if (!((StatefulNetworkProcess) instance).shuttingDown() && instance.connectionCount() < optimalConnections) {
                        closed++;
                        instance.stop();
                    }
                }
                i++;
            }
            if (i == 10_000) {
                LOGGER.warn("Possible stop algorithm error");
            }
            return;
        }

        /* Make sure we have the minimum amount of Instances */
        int register = startupInstances - instances;
        for (int i = 0; i < register; i++) {
            startNewInstance();
        }

        /* If all instances less connections then the optimalConnections and maxInstances() is not reach start new instance */
        if (statistics.getMinimumConnections(service) > optimalConnections && instances < maxInstances) {
            startNewInstance();
        }
    }

}
