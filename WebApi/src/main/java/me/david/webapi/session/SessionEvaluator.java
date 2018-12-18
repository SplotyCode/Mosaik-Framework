package me.david.webapi.session;

import me.david.webapi.request.Request;

public interface SessionEvaluator {

    boolean valid(Session session, Request request);

}
