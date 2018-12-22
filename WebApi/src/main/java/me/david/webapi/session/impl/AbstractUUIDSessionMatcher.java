package me.david.webapi.session.impl;

import me.david.webapi.request.Request;
import me.david.webapi.session.Session;
import me.david.webapi.session.SessionMatcher;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractUUIDSessionMatcher implements SessionMatcher {

    protected ConcurrentHashMap<UUID, Session> sessions = new ConcurrentHashMap<>();

    public abstract UUID getUUID(Request request);
    public abstract UUID generateUUID(Request request);

    @Override
    public Session getSession(Request request) {
        return sessions.get(getUUID(request));
    }

    @Override
    public void register(Session session, Request request) {
        sessions.put(generateUUID(request), session);
    }

    @Override
    public void unregister(Session session, Request request) {
        sessions.remove(getUUID(request));
    }

}
