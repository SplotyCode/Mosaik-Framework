package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

public interface SessionEvaluator {

    boolean valid(Session session, Request request);

}
