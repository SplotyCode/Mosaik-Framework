package io.github.splotycode.mosaik.networking.host;

import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.StaticHealthCheck;
import io.github.splotycode.mosaik.networking.util.IpResolver;
import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.listener.ListenerHandler;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SelfHost implements Host {

    private StaticHealthCheck healthCheck = new StaticHealthCheck(true);

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    private boolean localAddress;
    @Setter private Cache<InetAddress> localAddressCache;
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
                localAddressCache = DefaultCaches.getTimeCache(cache -> {
                    try {
                        return InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }, cacheTime);
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
    public InetAddress address() {
        try {
            return localAddress ?
                    (localAddressCache == null ? InetAddress.getLocalHost() : localAddressCache.getValue()) :
                    InetAddress.getByName(ipResolver.getIpAddress());
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @Override
    public ListenerHandler handler() {
        return handler;
    }
}
