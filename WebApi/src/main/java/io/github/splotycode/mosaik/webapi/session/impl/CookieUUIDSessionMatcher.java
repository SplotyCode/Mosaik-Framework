package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CookieUUIDSessionMatcher extends AbstractUUIDSessionMatcher {

    private final CookieKey mostKey, leastKey;

    public CookieUUIDSessionMatcher() {
        this("api_identifier", true);
    }

    public CookieUUIDSessionMatcher(String cookieName, boolean secure) {
        mostKey = new CookieKey(cookieName + "_most", secure, true);
        leastKey = new CookieKey(cookieName + "_least", secure, true);
    }

    @Override
    public UUID getUUID(Request request) {
        String mostStr = request.getCookie(mostKey);
        String leastStr = request.getCookie(leastKey);
        if (leastStr != null && mostStr != null) {
            return new UUID(Long.parseLong(mostStr), Long.parseLong(leastStr));
        }
        return null;
    }

    @Override
    public UUID generateUUID(Request request) {
        UUID uuid = UUID.randomUUID();
        request.getResponse().setCookie(mostKey, String.valueOf(uuid.getMostSignificantBits()));
        request.getResponse().setCookie(leastKey, String.valueOf(uuid.getLeastSignificantBits()));
        return uuid;
    }

}
