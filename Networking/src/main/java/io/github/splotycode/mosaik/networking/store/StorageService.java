package io.github.splotycode.mosaik.networking.store;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.NetworkComponent;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.statistics.SingleComponentService;

public class StorageService implements SingleComponentService {

    @Override
    public CloudKit kit() {

    }

    @Override
    public NetworkComponent component() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public ServiceStatus getStatus() {
        return null;
    }

    @Override
    public String statusMessage() {
        return null;
    }
}
