package me.david.webapi.session;

import me.david.webapi.request.Request;

public interface SessionMatcher {

    Session getSession(Request request);

    void unregister(Session session, Request request);

}
