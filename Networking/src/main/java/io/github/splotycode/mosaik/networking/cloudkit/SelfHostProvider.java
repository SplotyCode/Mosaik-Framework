package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.host.Host;

public interface SelfHostProvider {

    Host provide(CloudKit kit);

}
