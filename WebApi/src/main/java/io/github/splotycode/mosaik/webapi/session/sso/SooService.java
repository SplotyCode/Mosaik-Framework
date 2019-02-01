package io.github.splotycode.mosaik.webapi.session.sso;

import io.github.splotycode.mosaik.webapi.session.SessionEvaluator;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;
import io.github.splotycode.mosaik.webapi.session.impl.LocalSession;
import io.github.splotycode.mosaik.webapi.session.impl.StaticSessionSystem;
import lombok.Getter;

@Getter
public class SooService {

    private SessionEvaluator sessionEvaluator;

    private SessionSystem serviceSystem;
    private SessionSystem clientSystem;

    public SooService(SessionEvaluator sessionEvaluator, SessionMatcher sessionMatcher, String serviceUrl, String loginUrl, String clientCallback, String afterLogin) {
        this.sessionEvaluator = sessionEvaluator;
        clientSystem = new StaticSessionSystem(request -> {
            if (request.getPath().equalsIgnoreCase(clientCallback)) {
                return new LocalSession();
            }
            request.getResponse().redirect(serviceUrl + "?redirect=" + request.getFullUrl(), false);
            return null;
        }, sessionMatcher, sessionEvaluator);
        serviceSystem = new StaticSessionSystem(request -> {
            if (request.getPath().equalsIgnoreCase(serviceUrl)) {
                request.getResponse().redirect(loginUrl + "?redirect=" + request.getFirstGetParameter("redirect"), false);
            }
            return null;
        }, sessionMatcher, sessionEvaluator);
    }
}
