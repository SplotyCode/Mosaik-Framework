package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitComponent;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalHost;

public interface MasterHost extends StatisticalHost, CloudKitComponent {

    default void startNewInstance(String service) {
        startNewInstance(cloudKit().getServiceByName(service));
    }

    void startNewInstance(Service service);

    void stopService(String service, int port);

    void sendPacket(SerializedPacket packet);

    void initialize(MasterService service);

    default void tryInitialize() {
        MasterService masterService = cloudKit().getServiceByClass(MasterService.class);
        if (masterService != null) {
            initialize(masterService);
        }
    }

}
