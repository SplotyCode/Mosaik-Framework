package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class CloudStatistics {

    private CloudKit kit;

    public interface Cycler {

        boolean cycle(INetworkProcess instance);

    }

    public void cycle(Service service, Cycler cycler) {
        for (Host host : kit.getHosts()) {
            if (host instanceof StatisticalHost) {
                for (INetworkProcess instance : ((StatisticalHost) host).statistics().getInstances(service)) {
                    if (cycler.cycle(instance)) {
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<INetworkProcess> getInstancesUnder(Service service, int threshold) {
        ArrayList<INetworkProcess> under = new ArrayList<>();
        cycle(service, instance -> {
            if (instance.connectionCount() < threshold) {
                under.add(instance);
            }
            return false;
        });
        return under;
    }

    public int getMinimumConnections(Service service) {
        int minimum = Integer.MAX_VALUE;
        for (Host host : kit.getHosts()) {
            if (host instanceof StatisticalHost) {
                INetworkProcess instance = ((StatisticalHost) host).statistics().getService(service).lowestConnectionInstance();
                if (instance != null) {
                    minimum = Math.min(minimum, instance.connectionCount());
                }
            }
        }
        return minimum;
    }

    public int getTotalInstances(Service service) {
        int totalinstances = 0;
        for (Host host : kit.getHosts()) {
            if (host instanceof StatisticalHost) {
                totalinstances += ((StatisticalHost) host).statistics().totalInstances(service);
            }
        }
        return totalinstances;
    }

}
