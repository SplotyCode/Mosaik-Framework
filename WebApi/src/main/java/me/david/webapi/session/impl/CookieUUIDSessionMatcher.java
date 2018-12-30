package me.david.webapi.session.impl;

import lombok.Getter;
import me.david.webapi.request.Request;
import me.david.webapi.response.CookieKey;

import java.util.UUID;

@Getter
public class CookieUUIDSessionMatcher extends AbstractUUIDSessionMatcher {

    private final CookieKey mostKey, leastKey;

    public CookieUUIDSessionMatcher() {
        this("_api_identifier");
    }

    public CookieUUIDSessionMatcher(String cookieName) {
        mostKey = new CookieKey(cookieName + "_most", true, true);
        leastKey = new CookieKey(cookieName + "_least", true, true);
    }

    @Override
    public UUID getUUID(Request request) {
        long most = Long.valueOf(request.getCookies().get(mostKey.getName()));
        long least = Long.valueOf(request.getCookies().get(leastKey.getName()));
        return new UUID(most, least);
    }

    @Override
    public UUID generateUUID(Request request) {
        UUID uuid = UUID.randomUUID();
        request.getResponse().setCookie(mostKey, String.valueOf(uuid.getMostSignificantBits()));
        request.getResponse().setCookie(leastKey, String.valueOf(uuid.getLeastSignificantBits()));
        return null;
    }

}
