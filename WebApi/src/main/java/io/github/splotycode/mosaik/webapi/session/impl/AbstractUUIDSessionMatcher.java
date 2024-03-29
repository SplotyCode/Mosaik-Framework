package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.session.Session;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractUUIDSessionMatcher implements SessionMatcher {

    protected ConcurrentHashMap<UUID, Session> sessions = new ConcurrentHashMap<>();

    public abstract UUID getUUID(Request request);
    public abstract UUID generateUUID(Request request);

    @Override
    public Session getSession(Request request) {
        UUID uuid = getUUID(request);
        if (uuid == null) {
            return null;
        }
        return sessions.get(uuid);
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
