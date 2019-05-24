package io.github.splotycode.mosaik.networking.master;

import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.Listener;

public interface MasterChangeListener extends Listener {

    void onChange(boolean own, MosaikAddress current, MosaikAddress last);

}
