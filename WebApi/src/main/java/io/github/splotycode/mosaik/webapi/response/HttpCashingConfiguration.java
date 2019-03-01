package io.github.splotycode.mosaik.webapi.response;

import io.github.splotycode.mosaik.util.CodecUtil;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;

//TODO
@Getter
public class HttpCashingConfiguration {

    private long expires = -1;
    private boolean noCache, noStore, noTransform, onlyIfCashed, mustRevalidate, isPublic, isPrivate;
    private long maxAge = -1, maxStale = -1, minFresh = -1;

    private HashSet<ValidationMode> validationModes = new HashSet<>();
    private ETagMode eTagMode = ETagMode.SHA_512;

    public void apply(Response response) {
        response.cashing = this;
        if (expires != -1) {
            Date date = new Date(expires * 1000L + System.currentTimeMillis());
            response.setHeader(ResponseHeader.EXPIRES, Response.DATE_FORMAT.format(date));
        }
        StringBuilder sb = new StringBuilder();
        appendBoolean(sb, noCache, "no-cache");
        appendBoolean(sb, noStore, "no-store");
        appendBoolean(sb, noTransform, "no-transform");
        appendBoolean(sb, onlyIfCashed, "only-if-cached");
        appendBoolean(sb, mustRevalidate, "must-revalidate");
        appendBoolean(sb, isPublic, "public");
        appendBoolean(sb, isPrivate, "private");

        appendSeconds(sb, maxAge, "max-age");
        appendSeconds(sb, maxStale, "max-stale");
        appendSeconds(sb, minFresh, "min-fresh");

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
            response.setHeader(ResponseHeader.CACHE_CONTROL, sb.toString());
        }
    }

    private static void appendBoolean(StringBuilder sb, boolean bool, String name) {
        if (bool) {
            sb.append(name).append(", ");
        }
    }

    private static void appendSeconds(StringBuilder sb, long seconds, String name) {
        if (seconds != -1) {
            sb.append(name).append("=").append(seconds).append(", ");
        }
    }

    public String generateEtag(InputStream content) throws IOException {
        switch (eTagMode) {
            case MD5:
                return CodecUtil.md5Hex(content);
            case SHA_512:
                return CodecUtil.sha512Hex(content);
            case SHA_1:
                return CodecUtil.sha1Hex(content);
            case SHA_256:
                return CodecUtil.sha256Hex(content);
            default:
                throw new IllegalArgumentException("E-Tag mode " + eTagMode + " not supported");
        }
    }

    public enum ValidationMode {

        E_TAG,
        MODIFIED

    }

    public enum ETagMode {

        SHA_1,
        SHA_256,
        SHA_512,
        MD5

    }

    public HttpCashingConfiguration seteTagMode(ETagMode eTagMode) {
        this.eTagMode = eTagMode;
        return this;
    }

    public HttpCashingConfiguration setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public HttpCashingConfiguration setMaxStale(long maxStale) {
        this.maxStale = maxStale;
        return this;
    }

    public HttpCashingConfiguration setMinFresh(long minFresh) {
        this.minFresh = minFresh;
        return this;
    }

    public HttpCashingConfiguration setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public HttpCashingConfiguration setNoCache(boolean noCache) {
        this.noCache = noCache;
        return this;
    }

    public HttpCashingConfiguration setMustRevalidate(boolean mustRevalidate) {
        this.mustRevalidate = mustRevalidate;
        return this;
    }

    public HttpCashingConfiguration setNoTransform(boolean noTransform) {
        this.noTransform = noTransform;
        return this;
    }

    public HttpCashingConfiguration setNoStore(boolean noStore) {
        this.noStore = noStore;
        return this;
    }

    public HttpCashingConfiguration setOnlyIfCashed(boolean onlyIfCashed) {
        this.onlyIfCashed = onlyIfCashed;
        return this;
    }

    public HttpCashingConfiguration setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
        return this;
    }

    public HttpCashingConfiguration setValidationModes(HashSet<ValidationMode> validationModes) {
        this.validationModes = validationModes;
        return this;
    }

    public HttpCashingConfiguration setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

}
