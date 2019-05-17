package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.cloudkit.SelfHostProvider;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import lombok.Setter;

public class SelfHost implements Host {

    public static final SelfHostProvider PROVIDER = kit -> new SelfHost(kit.getLocalIpResolver());

    private StaticHealthCheck healthCheck = new StaticHealthCheck(true);

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    private boolean localAddress;
    @Setter private Cache<MosaikAddress> localAddressCache;
    @Setter private IpResolver ipResolver;

    public void setLocalAddress(IpResolver ipResolver) {
        localAddress = false;
        this.ipResolver = ipResolver;
    }

    public void setLocalAddress(boolean localAddress, int cacheTime) {
        this.localAddress = localAddress;
        if (localAddress) {
            if (cacheTime == 0) {
                localAddressCache = null;
            } else {
                localAddressCache = DefaultCaches.getTimeCache(cache -> MosaikAddress.local(), cacheTime);
            }
        } else {
            ipResolver = IpResolver.createDefaults().enableCaching(cacheTime);
        }
    }

    public SelfHost(boolean localAddress, int time) {
        setLocalAddress(localAddress, time);
    }

    public SelfHost(IpResolver ipResolver) {
        setLocalAddress(ipResolver);
    }

    public SelfHost(boolean localAddress) {
        this(localAddress, 0);
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public MosaikAddress address() {
        return localAddress ?
                (localAddressCache == null ? MosaikAddress.local() : localAddressCache.getValue()) :
                ipResolver.getIpAddress();
    }

    @Override
    public ListenerHandler handler() {
        return handler;
    }
}
