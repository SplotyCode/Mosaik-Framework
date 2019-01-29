package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.session.Session;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;

import java.util.concurrent.ConcurrentHashMap;

public abstract class StringSessionMatcher implements SessionMatcher {

    protected ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private CookieKey cookieKey;

    public StringSessionMatcher(String cookieName) {
        cookieKey = new CookieKey(cookieName, true, true);
    }

    public abstract String generateNew(Request request);

    @Override
    public Session getSession(Request request) {
        return sessions.get(request.getCookie(cookieKey));
    }

    @Override
    public void register(Session session, Request request) {
        sessions.put(generateNew(request), session);
    }

    @Override
    public void unregister(Session session, Request request) {
        sessions.remove(request.getCookie(cookieKey));
    }
}
