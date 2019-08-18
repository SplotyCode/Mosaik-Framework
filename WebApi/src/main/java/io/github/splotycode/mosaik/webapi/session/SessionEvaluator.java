package io.github.splotycode.mosaik.webapi.session;

import io.github.splotycode.mosaik.webapi.request.Request;

/**
 * The {@link SessionEvaluator} validates a Session
 * once for every Request (if {@link Request#getSession()} is called).
 * The Session will not be validated on the Request on that i was created.
 */
public interface SessionEvaluator {

    boolean valid(Session session, Request request);

}
