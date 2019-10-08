package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.Setter;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

//TODO: why not use cache from util
public abstract class HostMapProvider implements Cache<TreeMap<MosaikAddress, Host>>, AddressChangeListener {

    protected CloudKit kit;

    protected TreeMap<MosaikAddress, Host> hosts = new TreeMap<>();

    public HostMapProvider(CloudKit kit, long maxDelay) {
        this.kit = kit;
        this.maxDelay = maxDelay;
        kit.getHandler().addListener(this);
    }

    protected volatile AtomicLong lastClear = new AtomicLong(-1);
    @Setter protected long maxDelay;

    protected abstract List<String> fill();

    public void addHost(String host) {
        addHost(kit.getHostProvider().provide(kit, host));
    }

    public void addHost(Host host) {
        hosts.put(host.address(), host);
    }

    protected final void reFill() {
        List<String> list = fill();
        for (String host : list) {
            hosts.putIfAbsent(new MosaikAddress(host), kit.getHostProvider().provide(kit, host));
        }
        hosts.entrySet().removeIf(host -> !list.contains(host.getKey().asString()));

        Host self = kit.getSelfHost();
        if (self != null) {
            hosts.putIfAbsent(self.address(), self);
        }
    }

    @Override
    public TreeMap<MosaikAddress, Host> getValue() {
        lastClear.getAndUpdate(last -> {
            if (last + maxDelay < System.currentTimeMillis()) {
                reFill();
                return System.currentTimeMillis();
            }
            return last;
        });
        return hosts;
    }

    @Override
    public void onChange(MosaikAddress oldAddress, MosaikAddress newAddress) {
        Host host = hosts.remove(oldAddress);
        hosts.put(newAddress, host);
    }

    @Override
    public void clear() {
        hosts.clear();
    }

    @Override
    public void setValue(TreeMap<MosaikAddress, Host> value) {
        hosts = value;
    }

    @Override
    public ListenerHandler<CacheListener<TreeMap<MosaikAddress, Host>>> getHandler() {
        return DummyListenerHandler.dummy();
    }
}
