package io.github.splotycode.mosaik.webapi.request;

import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.webapi.request.body.RequestBodyHelper;
import io.github.splotycode.mosaik.webapi.request.body.RequestContent;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.github.splotycode.mosaik.webapi.session.Session;
import io.github.splotycode.mosaik.webapi.session.SessionCreator;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@EqualsAndHashCode
public abstract class AbstractRequest implements Request {

    protected Response response = new Response(null);
    protected RequestContent content = null;
    private WebServer webServer;

    private boolean checkedSession;
    @Setter private Session session = null;
    private String fullUrl;

    protected DataFactory dataFactory = new DataFactory();

    public AbstractRequest(WebServer webServer, String fullUrl) {
        this.webServer = webServer;
        this.fullUrl = fullUrl;
    }

    protected void ensureRequestContent() {
        if (content == null) {
            content = RequestBodyHelper.getRequestContent(this, webServer);
        }
    }

    @Override
    public RequestContent getContent() {
        ensureRequestContent();
        return content;
    }

    public String getHeader(String name) {
        return getHeaders().get(name);
    }

    public String getHeader(RequestHeader header) {
        return getHeader(EnumUtil.toDisplayName(header));
    }

    public Collection<String> getGetParameter(String name) {
        return getGet().get(name);
    }

    public Collection<String> getPostParameter(String name) {
        return getPost().get(name);
    }

    public String getFirstGetParameter(String name) {
        Collection<String> parameters = getGetParameter(name);
        if (parameters == null) return null;
        return CollectionUtil.getAny(parameters);
    }

    public String getFirstPostParameter(String name) {
        Collection<String> parameters = getPostParameter(name);
        if (parameters == null) return null;
        return CollectionUtil.getAny(parameters);
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
            for (SessionSystem system : webServer.getSessionSystems()) {
                Session matched = system.getSessionMatcher().getSession(this);
                if (matched != null) {
                    if (system.getSessionEvaluator().valid(matched, this)) {
                        session = matched;
                        session.onRefresh(this);
                    } else {
                        system.destroy(this, matched);
                    }
                }
                SessionCreator creator = system.getSessionCreator();
                if (session == null && creator.autoCreate()) {
                    Session newSession = creator.createSession(this);
                    if (newSession != null) {
                        system.start(this);
                    }
                }
            }
        }
        return session;
    }

    @Override
    public boolean hasPermission(String permission) {
        for (SessionSystem system : webServer.getSessionSystems()) {
            if (!system.hasPermission(this, permission)) {
                return false;
            }
        }
        return !webServer.getSessionSystems().isEmpty();
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
