package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.host.Host;

public interface HostProvider {

    Host provide(String host);

}
