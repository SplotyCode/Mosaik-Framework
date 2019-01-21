package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface SessionMatcher {

    Session getSession(Request request);

    void register(Session session, Request request);
    void unregister(Session session, Request request);

}
