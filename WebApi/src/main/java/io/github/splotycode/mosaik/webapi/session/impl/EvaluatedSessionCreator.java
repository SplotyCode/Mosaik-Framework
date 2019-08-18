package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.session.Session;
import io.github.splotycode.mosaik.webapi.session.SessionCreator;
import io.github.splotycode.mosaik.webapi.session.SessionEvaluator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class EvaluatedSessionCreator implements SessionCreator {

    protected SessionEvaluator evaluator;

    @Override
    public final Session createSession(Request request) {
        return evaluator.valid(null, request) ? newSession(request) : null;
    }

    @Override
    public boolean autoCreate() {
        return true;
    }

    public abstract Session newSession(Request request);

}
