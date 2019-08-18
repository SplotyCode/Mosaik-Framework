package io.github.splotycode.mosaik.webapi.request;

import io.github.splotycode.mosaik.util.datafactory.DataFactoryComponent;
import io.github.splotycode.mosaik.webapi.handler.UrlPattern;
import io.github.splotycode.mosaik.webapi.response.CookieKey;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import io.github.splotycode.mosaik.webapi.request.body.RequestContent;
import io.github.splotycode.mosaik.webapi.session.Session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface Request extends DataFactoryComponent {

    WebServer getWebServer();

    String getPath();

    default String getSimplifiedPath() {
        return UrlPattern.simplify(getPath());
    }

    String getFullUrl();
    Method getMethod();

    Response getResponse();

    String getIpAddress();

    byte[] getBody();
    RequestContent getContent();

    boolean isKeepAlive();

    String getHeader(String name);
    String getHeader(RequestHeader header);
    HashMap<String, String> getHeaders();

    Collection<String> getGetParameter(String name);
    Collection<String> getPostParameter(String name);

    String getFirstGetParameter(String name);
    String getFirstPostParameter(String name);

    boolean isGet();
    boolean isPost();

    Map<String, ? extends Collection<String>> getGet();
    Map<String, ? extends Collection<String>> getPost();

    Map<String, String> getCookies();
    String getCookie(String name);
    String getCookie(CookieKey key);

    void setPost(Map<String, ? extends Collection<String>> parameters);

    Session getSession();
    void setSession(Session session);

}
