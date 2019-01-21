package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.session.SessionEvaluator;
import io.github.splotycode.mosaik.webapi.session.SessionCreator;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;

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
