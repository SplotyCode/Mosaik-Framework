package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.service.Service;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class CloudStatistics {

    private CloudKit kit;

    public interface Cycler {

        boolean cycle(Instance instance);

    }

    public void cycle(Service service, Cycler cycler) {
        for (Host host : kit.getHosts()) {
            if (host instanceof StatisticalHost) {
                for (Instance instance : ((StatisticalHost) host).getStatistics().getConnection(service)) {
                    if (cycler.cycle(instance)) {
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<Instance> getInstancesUnder(Service service, int threashold) {
        ArrayList<Instance> under = new ArrayList<>();
        cycle(service, instance -> {
            if (instance.getConnections() < threashold) {
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
                for (Instance connection : ((StatisticalHost) host).getStatistics().getConnection(service)) {
                    minimum = Math.min(minimum, connection.getConnections());
                }
            }
        }
        return minimum;
    }

    public int getTotalInstances(Service service) {
        int totalinstances = 0;
        for (Host host : kit.getHosts()) {
            if (host instanceof StatisticalHost) {
                totalinstances += ((StatisticalHost) host).getStatistics().getInstances(service);
            }
        }
        return totalinstances;
    }

}
