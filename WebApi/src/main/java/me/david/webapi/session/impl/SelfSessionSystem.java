package me.david.webapi.session.impl;

import me.david.webapi.session.SessionCreator;
import me.david.webapi.session.SessionEvaluator;
import me.david.webapi.session.SessionMatcher;
import me.david.webapi.session.SessionSystem;

public interface SelfSessionSystem extends SessionSystem, SessionCreator, SessionEvaluator, SessionMatcher {

    @Override
    default SessionCreator getSessionCreator() {
        return this;
    }

    @Override
    default SessionEvaluator getSessionEvaluator() {
        return this;
    }

    @Override
    default SessionMatcher getSessionMatcher() {
        return this;
    }
}
