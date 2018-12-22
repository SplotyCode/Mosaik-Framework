package me.david.webapi.session.impl;

import me.david.webapi.request.Request;
import me.david.webapi.session.Session;
import me.david.webapi.session.SessionMatcher;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleSessionMatcher implements SessionMatcher {

    private static final String COOKIE_NAME = "_api_session";

    private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public void unregister(Session session, Request request) {
        sessions.remove(request.getCookies().get(COOKIE_NAME));
    }

    @Override
    public void register(Session session, Request request) {
        sessions.put(request.getCookies().get(COOKIE_NAME), session);
    }

    @Override
    public Session getSession(Request request) {
        return sessions.get(request.getCookies().get(COOKIE_NAME));
    }

}
