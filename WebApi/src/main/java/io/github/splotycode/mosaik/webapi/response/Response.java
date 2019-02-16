package io.github.splotycode.mosaik.webapi.response;

import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.ContentException;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.string.StaticStringContent;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@EqualsAndHashCode
public class Response {

    private static Map<String, CookieKey> CACHED_COOKIE_KEYS = new HashMap<>();

    private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    private static final Calendar CALENDAR = new GregorianCalendar();

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
    }

    @Getter private HttpVersion httpVersion = HttpVersion.VERSION_1_1;
    private Map<String, String> headers = new HashMap<>();
    private Map<CookieKey, String> setCookies = new HashMap<>();
    @Setter private ResponseContent content;
    private InputStream rawContent;
    @Setter private int responseCode = 200;

    public Response(ResponseContent content) {
        this.content = content;
        setContentType(ContentType.TEXT_HTML);

        /* Default Headers */
        setHeader(ResponseHeaders.DATE, DATE_FORMAT.format(CALENDAR.getTime()));
        setHeader("x-xss-protection", "1; mode=block");
        setHeader("X-Content-Type-Options", "nosniff");
        setHeader("X-Powered-By", "Mosaik WebApi");
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

    public Response setHeader(ResponseHeaders httpHeader, String value) {
        headers.put(EnumUtil.toDisplayName(httpHeader), value);
        return this;
    }

    public Response setHeader(String httpHeader, String value) {
        if (value == null) throw new HandleRequestException("Can not set a header to Null");
        headers.put(httpHeader, value);
        return this;
    }

    public Response setContentType(ContentType contentType) {
        setHeader(ResponseHeaders.CONTENT_TYPE, contentType.value());
        return this;
    }

    public void finish(Request request, WebServer server) {
        if (content == null) {
            content = server.getConfig().getDataDefault(WebConfig.NO_CONTENT_RESPONSE);
            if (content == null) {
                content = new StaticStringContent("No Content Provided");
            }
        }
        try {
            rawContent = content.getInputStream();
            setHeader(ResponseHeaders.CONTENT_LENGTH, String.valueOf(rawContent.available()));
        } catch (IOException ex) {
           throw new ContentException("Could not load content", ex);
        }
        try {
            String contentType = content.getContentType();
            if (contentType != null) {
                setHeader(ResponseHeaders.CONTENT_TYPE, contentType);
            }
        } catch (IOException ex) {
            throw new ContentException("Could not set content type", ex);
        }
        if (request != null && request.isKeepAlive()) {
            setHeader(ResponseHeaders.CONNECTION, "keep-alive");
        }
    }

    public void redirect(String url, int errorCode) {
        responseCode = errorCode;
        setHeader(ResponseHeaders.LOCATION, url);
    }

    public void redirect(String url, boolean permanent) {
        redirect(url, permanent ? 308 : 307);
    }



}
