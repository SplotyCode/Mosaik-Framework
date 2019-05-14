package io.github.splotycode.mosaik.networking.service;

import io.github.splotycode.mosaik.networking.component.NetworkComponent;

public interface SingleComponentService extends Service {

    NetworkComponent component();

}
