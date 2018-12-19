package me.david.webapi.session;

public interface SessionSystem {

    SessionCreator getSessionCreator();

    SessionMatcher getSessionMatcher();

    SessionEvaluator getSessionEvaluator();

}
