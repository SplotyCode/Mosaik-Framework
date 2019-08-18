package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface SessionSystem {

    default void destroy(Request request) {
        destroy(request, request.getSession());
    }

    default void destroy(Request request, Session session) {
        session.onDestruction();
        getSessionMatcher().unregister(session, request);
    }

    default boolean start(Request request) {
        Session newSession = getSessionCreator().createSession(request);
        if (newSession != null) {
            request.setSession(newSession);
            newSession.onInit(request);
            getSessionMatcher().register(newSession, request);
            return true;
        }
        return false;
    }

    SessionCreator getSessionCreator();

    SessionMatcher getSessionMatcher();

    SessionEvaluator getSessionEvaluator();

}
