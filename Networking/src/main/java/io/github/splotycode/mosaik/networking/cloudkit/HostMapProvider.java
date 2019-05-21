package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.CacheListener;
import io.github.splotycode.mosaik.util.listener.DummyListenerHandler;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import lombok.Setter;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class HostMapProvider implements Cache<TreeMap<MosaikAddress, Host>>, AddressChangeListener {

    protected CloudKit kit;

    public HostMapProvider(CloudKit kit, long maxDelay) {
        this.kit = kit;
        this.maxDelay = maxDelay;
    }

    protected TreeMap<MosaikAddress, Host> hosts = new TreeMap<>();

    protected volatile AtomicLong lastClear = new AtomicLong(-1);
    @Setter protected long maxDelay;

    protected abstract void fill();

    public void addHost(Host host) {
        host.handler().addListener(this);
        hosts.put(host.address(), host);
    }

    protected final void reFill() {
        clear();
        addHost(kit.getSelfHost());
        fill();
    }

    @Override
    public TreeMap<MosaikAddress, Host> getValue() {
        lastClear.getAndUpdate(last -> {
            if (last + maxDelay > System.currentTimeMillis()) {
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
        hosts.values().forEach(host -> host.handler().removeListener(this));
        hosts.clear();
    }

    @Override public void setValue(TreeMap<MosaikAddress, Host> value) {}

    @Override
    public ListenerHandler<CacheListener<TreeMap<MosaikAddress, Host>>> getHandler() {
        return DummyListenerHandler.dummy();
    }
}
