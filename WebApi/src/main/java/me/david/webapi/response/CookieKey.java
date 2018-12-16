package me.david.webapi.response;

import io.netty.handler.codec.DateFormatter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CookieKey {

    private final String name;
    private final boolean secure, httpOnly;
    private final long maxAge;
    private final String domain, path;

    public CookieKey(String name) {
        this.name = name;
        secure = httpOnly = false;
        maxAge = -1;
        domain = path = null;
    }

    public CookieKey(String name, String path) {
        this.name = name;
        this.path = path;
        secure = httpOnly = false;
        maxAge = -1;
        domain = null;
    }

    public CookieKey(String name, boolean secure, boolean httpOnly) {
        this.name = name;
        this.secure = secure;
        this.httpOnly = httpOnly;
        maxAge = -1;
        domain = path = null;
    }

    public CookieKey(String name, boolean secure, boolean httpOnly, long maxAge) {
        this.name = name;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.maxAge = maxAge;
        domain = path = null;
    }


    public String toHeaderString(String value) {
        if (value == null) value = "";
        StringBuilder builder = new StringBuilder(name);
        builder.append("=\"").append(value).append("\"; ");
        if (maxAge != -1) {
            builder.append("Max-Age=").append(maxAge).append("; ");
            Date expires = new Date(maxAge * 1000L + System.currentTimeMillis());
            builder.append("Expires=").append(Response.DATE_FORMAT.format(expires)).append("; ");
        }
        if (path != null) {
            builder.append("Path=").append(path).append("; ");
        }
        if (domain != null) {
            builder.append("Domain=").append(domain).append("; ");
        }
        if (secure) {
            builder.append("Secure; ");
        }
        if (httpOnly) {
            builder.append("HTTPOnly; ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }


}
