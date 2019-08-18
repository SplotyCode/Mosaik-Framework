package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

/**
 * The SessionMatcher matches Sessions over multiple Requests.
 * @see io.github.splotycode.mosaik.webapi.session.impl.CookieUUIDSessionMatcher
 */
public interface SessionMatcher {

    Session getSession(Request request);

    void register(Session session, Request request);
    void unregister(Session session, Request request);

}
