package io.github.splotycode.mosaik.webapi.request;

import io.github.splotycode.mosaik.webapi.request.body.RequestBodyHelper;
import io.github.splotycode.mosaik.webapi.request.body.RequestContent;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.github.splotycode.mosaik.webapi.session.Session;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;

import java.util.Collection;

@Getter
@EqualsAndHashCode
public abstract class AbstractRequest implements Request {

    protected Response response = new Response(null);
    protected RequestContent content = null;
    private WebServer webServer;

    private boolean checkedSession;
    private Session session = null;
    private String fullUrl;

    public AbstractRequest(WebServer webServer, String fullUrl) {
        this.webServer = webServer;
        this.fullUrl = fullUrl;
    }

    @Override
    public RequestContent getContent() {
        if (content == null) {
            content = RequestBodyHelper.getRequestContent(this, webServer);
        }
        return content;
    }

    public String getHeader(String name) {
        return getHeaders().get(name);
    }

    public String getHeader(RequestHeaders header) {
        return getHeaders().get(EnumUtil.toDisplayName(header));
    }

    public Collection<String> getGetParameter(String name) {
        return getGet().get(name);
    }

    public Collection<String> getPostParameter(String name) {
        return getPost().get(name);
    }

    public String getFirstGetParameter(String name) {
        return getGetParameter(name).iterator().next();
    }

    public String getFirstPostParameter(String name) {
        return getPostParameter(name).iterator().next();
    }

    public boolean isGet() {
        return getMethod().isStandard() && getMethod().getStandardMethod() == Method.StandardMethod.GET;
    }

    public boolean isPost() {
        return getMethod().isStandard() && getMethod().getStandardMethod() == Method.StandardMethod.POST;
    }

    @Override
    public Session getSession() {
        if (!checkedSession) {
            checkedSession = true;
            for (SessionSystem sessionSystem : webServer.getSessionSystems()) {
                Session matcher = sessionSystem.getSessionMatcher().getSession(this);
                if (matcher == null) {
                    Session newSession = sessionSystem.getSessionCreator().createSession(this);
                    if (newSession != null) {
                        session = newSession;
                        session.onInit(this);
                        sessionSystem.getSessionMatcher().register(session, this);
                    }
                } else if (sessionSystem.getSessionEvaluator().valid(matcher, this)) {
                    session = matcher;
                    session.onRefresh(this);
                } else {
                    matcher.onDestruction();
                    sessionSystem.getSessionMatcher().unregister(matcher, this);
                }
            }
            return null;
        }
        return session;
    }

    @Override
    public String getCookie(String name) {
        return getCookies().get(name);
    }

    @Override
    public String getCookie(CookieKey key) {
        return getCookie(key.getName());
    }

}
