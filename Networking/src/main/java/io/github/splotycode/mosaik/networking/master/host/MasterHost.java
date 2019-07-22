package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;

public interface MasterHost extends StatisticalHost {

    CloudKit getCloudKit();

    default void startNewInstance(String service) {
        startNewInstance(getCloudKit().getServiceByName(service));
    }

    void startNewInstance(Service service);

    void stopService(String service, int port);

}
