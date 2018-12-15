package me.david.webapi.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.davidlib.utils.EnumUtil;
import me.david.webapi.request.body.EmptyRequestContent;
import me.david.webapi.request.body.RequestBodyHelper;
import me.david.webapi.request.body.RequestContent;
import me.david.webapi.request.body.RequestContentHandler;
import me.david.webapi.response.Response;
import me.david.webapi.server.AbstractWebServer;
import me.david.webapi.server.WebServer;

import java.util.Collection;
import java.util.List;

@Getter
@EqualsAndHashCode
public abstract class AbstractRequest implements Request {

    protected Response response = new Response(null);
    protected RequestContent content = null;
    private WebServer webServer;

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
}
