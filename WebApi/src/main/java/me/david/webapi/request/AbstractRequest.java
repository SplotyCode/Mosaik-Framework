package me.david.webapi.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.davidlib.util.EnumUtil;
import me.david.webapi.request.body.RequestBodyHelper;
import me.david.webapi.request.body.RequestContent;
import me.david.webapi.response.Response;
import me.david.webapi.server.WebServer;
import me.david.webapi.session.Session;
import me.david.webapi.session.SessionSystem;

import java.util.Collection;

@Getter
@EqualsAndHashCode
public abstract class AbstractRequest implements Request {

    protected Response response = new Response(null);
    protected RequestContent content = null;
    private WebServer webServer;

    private boolean checkedSession;
    private Session session = null;

    public AbstractRequest(WebServer webServer) {
        this.webServer = webServer;
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

}
