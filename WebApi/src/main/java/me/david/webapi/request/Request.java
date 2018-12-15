package me.david.webapi.request;

import me.david.webapi.request.body.RequestContent;
import me.david.webapi.response.Response;
import me.david.webapi.server.WebServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Request {

    WebServer getWebServer();

    String getPath();
    Method getMethod();

    Response getResponse();

    String getIpAddress();

    byte[] getBody();
    RequestContent getContent();

    boolean isKeepAlive();

    String getHeader(String name);
    String getHeader(RequestHeaders header);
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

    void setPost(Map<String, ? extends Collection<String>> parameters);

}
