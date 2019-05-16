package io.github.splotycode.mosaik.networking.util;

import io.github.splotycode.mosaik.util.cache.Cache;
import io.github.splotycode.mosaik.util.cache.DefaultCaches;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class IpResolver {

    private static class LocalIpResolver extends IpResolver {

        @Override
        public MosaikAddress getIpAddress() {
            return MosaikAddress.local();
        }

    }

    private static final Logger LOGGER = Logger.getInstance(IpResolver.class);

    public static final IpResolver GLOBAL = createDefaults();

    public static final String DEFAULT_PREFERRED = "http://checkip.amazonaws.com/";

    @Getter private static final String[] DEFAULT = new String[]{
            "http://icanhazip.com/",
            "http://www.trackip.net/ip",
            "http://myexternalip.com/raw",
            "http://ipecho.net/plain",
            "http://bot.whatismyipaddress.com",
            "https://ident.me",
            "http://curlmyip.com/" /* Seems to be Offline */
    };

    private IpResolver(String preferred, String[] failover) {
        this.preferred = preferred;
        this.failover = failover;
    }

    private String preferred;
    private String[] failover;
    private Cache<MosaikAddress> cache;

    public static IpResolver createDefaults() {
        return new IpResolver(DEFAULT_PREFERRED, DEFAULT);
    }

    public static IpResolver createLocal() {
        return new LocalIpResolver();
    }

    public static IpResolver create(String preferred, String... others) {
        return new IpResolver(preferred, others);
    }

    public Cache<MosaikAddress> createTimeCache(long maxAge) {
        return DefaultCaches.getTimeCache(cache -> getIpFresh(), maxAge);
    }

    public IpResolver enableCaching(Cache<MosaikAddress> cache) {
        this.cache = cache;
        return this;
    }

    public IpResolver enableCaching(long maxAge) {
        cache = createTimeCache(maxAge);
        return this;
    }

    public IpResolver setPreferred(String preferred) {
        this.preferred = preferred;
        return this;
    }

    public IpResolver setFailover(String[] failover) {
        this.failover = failover;
        return this;
    }

    public MosaikAddress getIpAddress() {
        if (cache == null) {
            return getIpFresh();
        }
        return cache.getValue();
    }

    public MosaikAddress getIpFresh() {
        try {
            return getIpAddressByUrl(preferred);
        } catch (IOException ex) {
            LOGGER.warn("Failed to get Remote Ip from Preferred URL: " + preferred, ex);
            for (String url : failover) {
                try {
                    return getIpAddressByUrl(url);
                } catch (IOException ex2) {
                    LOGGER.warn("Failed to get Remote Ip from URL: " + url);
                }
            }
        }
        return null;
    }

    private static MosaikAddress getIpAddressByUrl(String url) throws IOException {
        URL website = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
        return new MosaikAddress(in.readLine());
    }

}
