package io.github.splotycode.mosaik.webapi.response;

import io.github.splotycode.mosaik.util.CodecUtil;
import io.github.splotycode.mosaik.webapi.request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HttpCashingConfiguration {

    public static final HttpCashingConfiguration ASSET_CASHING = getAssetsConfiguration();
    public static final HttpCashingConfiguration NO_CACHE = getNoCacheConfiguration();

    public static HttpCashingConfiguration getNoCacheConfiguration() {
        return new HttpCashingConfiguration()
                .setNoCache(true)
                .setNoStore(true)
                .setMustRevalidate(true);
    }

    public static HttpCashingConfiguration getAssetsConfiguration() {
        return new HttpCashingConfiguration()
                .setMaxAge(31536000)
                .setPublic(true)
                .addValidationModes(ValidationMode.E_TAG, ValidationMode.MODIFIED)
                .setETagMode(DefaultETagMode.SHA_1);
    }

    private long expires = -1;
    private boolean noCache, noStore, noTransform, onlyIfCashed, mustRevalidate, isPublic, isPrivate;
    private long maxAge = -1, maxStale = -1, minFresh = -1;

    private HashSet<ValidationMode> validationModes = new HashSet<>();
    private ETagMode eTagMode = DefaultETagMode.SHA_512;

    public void apply(Response response) {
        response.cashingConfiguration = this;
        if (expires != -1) {
            response.setHeader(ResponseHeader.EXPIRES, LocalDateTime.now(Response.ZONE_ID).plusNanos(expires * 1000 * 1000).format(Response.DATE_FORMAT));
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

    public enum ValidationMode {

        E_TAG,
        MODIFIED

    }

    public interface ETagMode {

        String getETag(Request request, InputStream stream) throws IOException;

    }

    public enum DefaultETagMode implements ETagMode {

        SHA_1 {
            @Override
            public String getETag(Request request, InputStream stream) throws IOException {
                return CodecUtil.sha1Hex(stream);
            }
        },
        SHA_256 {
            @Override
            public String getETag(Request request, InputStream stream) throws IOException {
                return CodecUtil.sha256Hex(stream);
            }
        },
        SHA_512 {
            @Override
            public String getETag(Request request, InputStream stream) throws IOException {
                return CodecUtil.sha512Hex(stream);
            }
        },
        MD5 {
            @Override
            public String getETag(Request request, InputStream stream) throws IOException {
                return CodecUtil.md5Hex(stream);
            }
        };

        @Override
        public abstract String getETag(Request request, InputStream stream) throws IOException;

    }

    public HttpCashingConfiguration setETagMode(ETagMode eTagMode) {
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

    public HttpCashingConfiguration addValidationModes(ValidationMode... validationModes) {
        this.validationModes.addAll(Arrays.asList(validationModes));
        return this;
    }

    public HttpCashingConfiguration setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

}
