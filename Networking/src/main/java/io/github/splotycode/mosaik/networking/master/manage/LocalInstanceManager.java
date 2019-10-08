package io.github.splotycode.mosaik.networking.master.manage;

import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@AllArgsConstructor
public class LocalInstanceManager<C extends INetworkProcess> {

    private MasterInstanceService<C> service;


    @Getter
    protected final HashMap<Integer, C> instances = new HashMap<>();

    public void startNewInstance() {
        int port = service.getPortSupplier().get();
        instances.put(port, service.createComponent(port));
    }

    public void stop(int port) {
        C instance = instances.remove(port);
        if (instance == null) throw new IllegalStateException("Tried to close a instance that does not exists");
        instance.stop();
    }

    public void stopAll() {
        instances.values().forEach(INetworkProcess::stop);
    }

}
