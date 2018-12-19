package me.david.webapi.session.impl;

import lombok.AllArgsConstructor;
import me.david.webapi.request.Request;
import me.david.webapi.session.Session;
import me.david.webapi.session.SessionCreator;
import me.david.webapi.session.SessionEvaluator;

@AllArgsConstructor
public abstract class EvaluatedSessionCreator implements SessionCreator {

    protected SessionEvaluator evaluator;

    @Override
    public final Session createSession(Request request) {
        return evaluator.valid(null, request) ? newSession(request) : null;
    }

    public abstract Session newSession(Request request);

}
