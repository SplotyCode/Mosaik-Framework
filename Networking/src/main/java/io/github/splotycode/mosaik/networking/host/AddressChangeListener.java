package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.util.listener.Listener;

import java.net.InetAddress;

public interface AddressChangeListener extends Listener {

    void onChange(InetAddress oldAddress, InetAddress newAddress);

}
