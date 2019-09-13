package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.service.Service;

public abstract class CloudKitService implements Service, CloudKitComponent {

    protected CloudKit cloudKit;

    protected void initialize(CloudKit cloudKit) {
        this.cloudKit = cloudKit;
    }

    @Override
    public CloudKit cloudKit() {
        return cloudKit;
    }
}
