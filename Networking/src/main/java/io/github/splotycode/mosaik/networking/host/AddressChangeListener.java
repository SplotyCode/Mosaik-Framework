package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface AddressChangeListener extends Listener {

    void onChange(MosaikAddress oldAddress, MosaikAddress newAddress);

}
