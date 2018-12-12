package me.david.webapi.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class CookieKey {

    private final String name;
    private final boolean secure, httpOnly;
    private final long expires, maxAge;
    private final String domain, path;

    public CookieKey(String name) {
        this.name = name;
        secure = httpOnly = false;
        expires = maxAge = -1;
        domain = path = null;
    }

    public CookieKey(String name, String path) {
        this.name = name;
        this.path = path;
        secure = httpOnly = false;
        expires = maxAge = -1;
        domain = null;
    }

    public CookieKey(String name, boolean secure, boolean httpOnly) {
        this.name = name;
        this.secure = secure;
        this.httpOnly = httpOnly;
        expires = maxAge = -1;
        domain = path = null;
    }

    public CookieKey(String name, boolean secure, boolean httpOnly, long maxAge) {
        this.name = name;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.maxAge = maxAge;
        domain = path = null;
        expires = -1;
    }




}
