package io.github.splotycode.mosaik.webapi.response;

import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.request.RequestHeader;
import io.github.splotycode.mosaik.webapi.response.content.ContentException;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.string.StaticStringContent;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@EqualsAndHashCode
public class Response {
    
    private static Map<String, CookieKey> CACHED_COOKIE_KEYS = new HashMap<>();

    private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    private static final Calendar CALENDAR = new GregorianCalendar();
    public static final ZoneId ZONE_ID = ZoneId.of(HTTP_DATE_GMT_TIMEZONE);

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
                                                .ofPattern(HTTP_DATE_FORMAT, Locale.US)
                                                .withZone(ZONE_ID);

    @Getter private HttpVersion httpVersion = HttpVersion.VERSION_1_1;
    private Map<CharSequence, CharSequence> headers = new HashMap<>();
    private Map<CookieKey, String> setCookies = new HashMap<>();
    @Setter private ResponseContent content;
    private InputStream rawContent = null;
    @Setter private int responseCode = 200;

    HttpCashingConfiguration cashingConfiguration;

    public Response(ResponseContent content) {
        this.content = content;
        setContentType(ContentType.TEXT_HTML);

        /* Default Headers */
        setHeader(ResponseHeader.DATE, DATE_FORMAT.format(LocalDateTime.now()));
        setHeader("x-xss-protection", "1; mode=block");
        setHeader("X-Content-Type-Options", "nosniff");
        setHeader("X-Powered-By", "Mosaik WebApi");
    }

    public Response applyCashingConfiguration(HttpCashingConfiguration cashingConfiguration) {
        this.cashingConfiguration = cashingConfiguration;
        cashingConfiguration.apply(this);
        return this;
    }

    public Response setCookie(CookieKey key, String value) {
        setCookies.put(key, value);
        return this;
    }

    public Response setCookie(String name, String value) {
        CookieKey key = CACHED_COOKIE_KEYS.computeIfAbsent(name, CookieKey::new);
        setCookies.put(key, value);
        return this;
    }

    public Response setHeader(ResponseHeader httpHeader, CharSequence value) {
        headers.put(EnumUtil.toDisplayName(httpHeader), value);
        return this;
    }

    public Response setHeader(CharSequence httpHeader, CharSequence value) {
        if (value == null) throw new HandleRequestException("Can not set a header to Null");
        headers.put(httpHeader, value);
        return this;
    }

    public Response setContentType(ContentType contentType) {
        setHeader(ResponseHeader.CONTENT_TYPE, contentType.value());
        return this;
    }

    public void finish(Request request, WebServer server) {
        /* Keep Alive */
        if (request != null && request.isKeepAlive()) {
            setHeader(ResponseHeader.CONNECTION, "keep-alive");
        }

        int userResponseCode = responseCode;

        /* Content  */
        if (content == null) {
            content = server.getConfig().getDataDefault(WebConfig.NO_CONTENT_RESPONSE);
            if (content == null) {
                content = new StaticStringContent("No Content Provided");
            }
        }

        /* Last Modified */
        boolean needUpdate = false;
        try {
            if (request != null && cashingConfiguration != null &&
                    cashingConfiguration.getValidationModes().contains(HttpCashingConfiguration.ValidationMode.MODIFIED)) {
                long lastModified = content.lastModified();
                if (lastModified != -1) {
                    String rawLastModified = request.getHeader(RequestHeader.IF_MODIFIED_SINCE);
                    try {
                        if (rawLastModified != null && LocalDateTime.parse(rawLastModified, DATE_FORMAT).atZone(ZONE_ID).toInstant().toEpochMilli() == lastModified) {
                            responseCode = HttpResponseStatus.NOT_MODIFIED.code();
                        } else {
                            setHeader(ResponseHeader.LAST_MODIFIED, Instant.ofEpochMilli(lastModified).atZone(ZONE_ID).format(DATE_FORMAT));
                            needUpdate = true;
                        }
                    } catch (NumberFormatException ex) {
                        throw new ContentException("Failed to parse " + rawLastModified + " as last modified", ex, 400);
                    }
                }
            }
        } catch (IOException ex) {
            throw new ContentException("Could not handle last-/cache modified", ex);
        }

        /* E-Tag */
        try {
            if (request != null && !needUpdate && cashingConfiguration != null &&
                    cashingConfiguration.getValidationModes().contains(HttpCashingConfiguration.ValidationMode.E_TAG)) {
                String currentETag = content.eTag(request, cashingConfiguration, () -> {
                    if (rawContent == null) {
                        try {
                            rawContent = new ByteArrayInputStream(IOUtil.toByteArray(loadContent()));
                        } catch (Throwable ex) {
                            throw new ContentException("Failed to convert stream to byte array", ex);
                        }
                    }
                    return new ByteArrayInputStream(((ByteArrayInputStream)rawContent).getOriginal());
                });
                String lastETag = request.getHeader(RequestHeader.IF_NONE_MATCH);
                if (currentETag != null) {
                   if (currentETag.equals(lastETag)) {
                       responseCode = HttpResponseStatus.NOT_MODIFIED.code();
                       return;
                   } else if (responseCode == HttpResponseStatus.NOT_MODIFIED.code()) {
                        responseCode = userResponseCode;
                   }
                }
                setHeader(ResponseHeader.ETAG, currentETag);
            }
        } catch (Exception ex) {
            throw new ContentException("Could not set E_TAG", ex);
        }

        if (userResponseCode != responseCode) {
            return;
        }

        /* Load content if not already loaded */
        if (rawContent == null) {
            rawContent = loadContent();
        }

        /* Content Length */
        try {
            setHeader(ResponseHeader.CONTENT_LENGTH, String.valueOf(rawContent.available()));
        } catch (IOException ex) {
            throw new ContentException("Could not set content length", ex);
        }

        /* Content Type */
        try {
            String contentType = content.getContentType();
            if (contentType != null) {
                setHeader(ResponseHeader.CONTENT_TYPE, contentType);
            }
        } catch (IOException ex) {
            throw new ContentException("Could not set content type", ex);
        }
    }

    private InputStream loadContent() {
        try {
            return content.getInputStream();
        } catch (IOException ex) {
            throw new ContentException("Could not load content", ex);
        }
    }

    public void redirect(String url, int errorCode) {
        responseCode = errorCode;
        setHeader(ResponseHeader.LOCATION, url);
    }

    public void redirect(String url, boolean permanent) {
        redirect(url, permanent ? 308 : 307);
    }



}
