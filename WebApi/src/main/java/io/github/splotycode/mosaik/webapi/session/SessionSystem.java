package io.github.splotycode.mosaik.webapi.session;

public interface SessionSystem {

    SessionCreator getSessionCreator();

    SessionMatcher getSessionMatcher();

    SessionEvaluator getSessionEvaluator();

}
